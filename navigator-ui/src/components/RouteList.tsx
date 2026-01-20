"use client";

import { useState, useEffect, useCallback } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { fetchRoutes, deleteRoute } from "@/lib/api";
import type { Route, RouteResponse } from "@/types";
import { Edit, Trash2, ChevronLeft, ChevronRight, ArrowUpDown } from "lucide-react";
import { toast } from "sonner";

interface RouteListProps {
  onEdit: (route: Route) => void;
  refreshTrigger: number;
}

export function RouteList({ onEdit, refreshTrigger }: RouteListProps) {
  const [data, setData] = useState<RouteResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [sort, setSort] = useState<string[]>([]);
  const [sortDirection, setSortDirection] = useState<"ASC" | "DESC">("ASC");
  const [filter, setFilter] = useState<string[]>([]);
  const [filterInput, setFilterInput] = useState("");

  const loadRoutes = useCallback(async () => {
    setLoading(true);
    try {
      const result = await fetchRoutes({
        page,
        pageSize,
        sort,
        sortDirection,
        filter,
      });
      setData(result);
    } catch (error) {
      console.error("Failed to fetch routes", error);
    } finally {
      setLoading(false);
    }
  }, [page, pageSize, sort, sortDirection, filter]);

  useEffect(() => {
    loadRoutes();
  }, [loadRoutes, refreshTrigger]);

  const handleDelete = async (id: number) => {
    if (confirm("Are you sure you want to delete this route?")) {
      try {
        await deleteRoute(id);
        toast.success("Route deleted successfully");
        loadRoutes();
      } catch (error: any) {
        toast.error(error.message || "Failed to delete route");
      }
    }
  };

  const toggleSort = (field: string) => {
    if (sort.includes(field)) {
      setSortDirection(prev => prev === "ASC" ? "DESC" : "ASC");
    } else {
      setSort([field]);
      setSortDirection("ASC");
    }
  };

  const applyFilter = () => {
    if (filterInput.trim()) {
      // Parse user-friendly filter format: field[operator]value
      // Supported: =, !=, >=, <=, >, <
      let backendFilter = filterInput.trim();
      
      const operators: Record<string, string> = {
        ">=": "gte",
        "<=": "lte",
        ">": "gt",
        "<": "lt",
        "!=": "ne",
        "=": "eq"
      };

      for (const [op, code] of Object.entries(operators)) {
        if (backendFilter.includes(op)) {
          const parts = backendFilter.split(op);
          if (parts.length === 2) {
            const field = parts[0].trim();
            const value = parts[1].trim();
            backendFilter = `${field}[${code}]=${value}`;
            break;
          }
        }
      }
      
      setFilter([backendFilter]);
    } else {
      setFilter([]);
    }
    setPage(0);
  };

  if (loading && !data) return <div>Loading...</div>;

  return (
    <div className="space-y-4">
      <div className="flex gap-2">
        <Input
          placeholder="Filter (e.g., name=test, distance>15, coordinates.x<100)"
          value={filterInput}
          onChange={(e) => setFilterInput(e.target.value)}
          className="max-w-sm"
        />
        <Button onClick={applyFilter}>Apply Filter</Button>
      </div>

      <div className="rounded-md border">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="cursor-pointer" onClick={() => toggleSort("id")}>
                ID <ArrowUpDown className="inline h-4 w-4" />
              </TableHead>
              <TableHead className="cursor-pointer" onClick={() => toggleSort("name")}>
                Name <ArrowUpDown className="inline h-4 w-4" />
              </TableHead>
              <TableHead className="cursor-pointer" onClick={() => toggleSort("coordinates")}>
                Coordinates <ArrowUpDown className="inline h-4 w-4" />
              </TableHead>
              <TableHead className="cursor-pointer" onClick={() => toggleSort("from")}>
                From <ArrowUpDown className="inline h-4 w-4" />
              </TableHead>
              <TableHead className="cursor-pointer" onClick={() => toggleSort("to")}>
                To <ArrowUpDown className="inline h-4 w-4" />
              </TableHead>
              <TableHead className="cursor-pointer" onClick={() => toggleSort("distance")}>
                Distance <ArrowUpDown className="inline h-4 w-4" />
              </TableHead>
              <TableHead className="cursor-pointer" onClick={() => toggleSort("creationDate")}>
                Creation Date <ArrowUpDown className="inline h-4 w-4" />
              </TableHead>
              <TableHead>Actions</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {data?.routes.map((route) => (
              <TableRow key={route.id}>
                <TableCell>{route.id}</TableCell>
                <TableCell className="font-medium">{route.name}</TableCell>
                <TableCell>({route.coordinates.x}, {route.coordinates.y})</TableCell>
                <TableCell>
                  {route.from ? `${route.from.name || ''} (${route.from.x}, ${route.from.y})` : "-"}
                </TableCell>
                <TableCell>
                  {route.to ? `${route.to.name || ''} (${route.to.x}, ${route.to.y}, ${route.to.z})` : "-"}
                </TableCell>
                <TableCell>{route.distance}</TableCell>
                <TableCell>{new Date(route.creationDate).toLocaleString()}</TableCell>
                <TableCell>
                  <div className="flex gap-2">
                    <Button variant="ghost" size="icon" onClick={() => onEdit(route)}>
                      <Edit className="h-4 w-4" />
                    </Button>
                    <Button variant="ghost" size="icon" onClick={() => handleDelete(route.id)}>
                      <Trash2 className="h-4 w-4 text-destructive" />
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
            {data?.routes.length === 0 && (
              <TableRow>
                <TableCell colSpan={8} className="text-center h-24">
                  No routes found.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>

      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-2">
          <span className="text-sm font-medium">Rows per page:</span>
          {[5, 10, 20, 50].map((size) => (
            <Button
              key={size}
              variant={pageSize === size ? "default" : "outline"}
              size="sm"
              onClick={() => {
                setPageSize(size);
                setPage(0);
              }}
            >
              {size}
            </Button>
          ))}
        </div>
        <div className="flex items-center space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => setPage(p => Math.max(0, p - 1))}
            disabled={page === 0}
          >
            <ChevronLeft className="h-4 w-4" /> Previous
          </Button>
          <span className="text-sm font-medium">Page {page + 1}</span>
          <Button
            variant="outline"
            size="sm"
            onClick={() => setPage(p => p + 1)}
            disabled={!data || data.routes.length < pageSize}
          >
            Next <ChevronRight className="h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  );
}
