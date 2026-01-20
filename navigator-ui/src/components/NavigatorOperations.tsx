"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { findRoutesBetweenLocations, addRouteBetweenLocations } from "@/lib/api";
import { toast } from "sonner";
import { Search, Plus } from "lucide-react";

export function NavigatorOperations({ onRouteAdded }: { onRouteAdded: () => void }) {
  const [idFrom, setIdFrom] = useState("");
  const [idTo, setIdTo] = useState("");
  const [orderBy, setOrderBy] = useState("id");
  const [sortDirection, setSortDirection] = useState<"ASC" | "DESC">("ASC");
  
  const [addIdFrom, setAddIdFrom] = useState("");
  const [addIdTo, setAddIdTo] = useState("");
  const [distance, setDistance] = useState("");
  const [routeName, setRouteName] = useState("");
  const [coordX, setCoordX] = useState("");
  const [coordY, setCoordY] = useState("");

  const [result, setResult] = useState<any>(null);
  const [loading, setLoading] = useState(false);

  const handleFindRoutes = async () => {
    if (!idFrom || !idTo || !orderBy) {
      toast.error("Please fill in all search fields");
      return;
    }
    setLoading(true);
    try {
      const res = await findRoutesBetweenLocations(
        Number(idFrom),
        Number(idTo),
        orderBy,
        sortDirection
      );
      setResult({ type: `Routes from ${idFrom} to ${idTo}`, data: res.routes });
      toast.success(`Found ${res.routes.length} routes`);
    } catch (e: any) {
      toast.error(e.message);
    } finally {
      setLoading(false);
    }
  };

  const handleAddRoute = async () => {
    if (!addIdFrom || !addIdTo || !distance || !routeName || !coordX || !coordY) {
      toast.error("Please fill in all fields to add a route");
      return;
    }
    setLoading(true);
    try {
      const res = await addRouteBetweenLocations(
        Number(addIdFrom),
        Number(addIdTo),
        Number(distance),
        {
          name: routeName,
          coordinates: { x: Number(coordX), y: Number(coordY) }
        }
      );
      setResult({ type: "Route Added via Navigator", data: res });
      toast.success("Route added successfully via second service");
      onRouteAdded();
      // Clear fields
      setRouteName("");
      setDistance("");
      setCoordX("");
      setCoordY("");
    } catch (e: any) {
      toast.error(e.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Search className="h-5 w-5" /> Find Routes Between Locations
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-2">
            <div className="space-y-1">
              <Label htmlFor="id-from">ID From</Label>
              <Input id="id-from" type="number" value={idFrom} onChange={(e) => setIdFrom(e.target.value)} />
            </div>
            <div className="space-y-1">
              <Label htmlFor="id-to">ID To</Label>
              <Input id="id-to" type="number" value={idTo} onChange={(e) => setIdTo(e.target.value)} />
            </div>
          </div>
          <div className="grid grid-cols-2 gap-2">
            <div className="space-y-1">
              <Label htmlFor="order-by">Order By</Label>
              <Input id="order-by" value={orderBy} onChange={(e) => setOrderBy(e.target.value)} placeholder="id, name, distance..." />
            </div>
            <div className="space-y-1">
              <Label htmlFor="dir">Direction</Label>
              <select 
                id="dir"
                className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
                value={sortDirection} 
                onChange={(e) => setSortDirection(e.target.value as "ASC" | "DESC")}
              >
                <option value="ASC">ASC</option>
                <option value="DESC">DESC</option>
              </select>
            </div>
          </div>
          <Button className="w-full" onClick={handleFindRoutes} disabled={loading}>Find Routes</Button>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Plus className="h-5 w-5" /> Add Route via Navigator
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-3 gap-2">
            <div className="space-y-1">
              <Label>ID From</Label>
              <Input type="number" value={addIdFrom} onChange={(e) => setAddIdFrom(e.target.value)} />
            </div>
            <div className="space-y-1">
              <Label>ID To</Label>
              <Input type="number" value={addIdTo} onChange={(e) => setAddIdTo(e.target.value)} />
            </div>
            <div className="space-y-1">
              <Label>Distance</Label>
              <Input type="number" value={distance} onChange={(e) => setDistance(e.target.value)} />
            </div>
          </div>
          <div className="space-y-1">
            <Label>New Route Name</Label>
            <Input value={routeName} onChange={(e) => setRouteName(e.target.value)} />
          </div>
          <div className="grid grid-cols-2 gap-2">
            <div className="space-y-1">
              <Label>Coord X</Label>
              <Input type="number" value={coordX} onChange={(e) => setCoordX(e.target.value)} step="any" />
            </div>
            <div className="space-y-1">
              <Label>Coord Y</Label>
              <Input type="number" value={coordY} onChange={(e) => setCoordY(e.target.value)} step="any" />
            </div>
          </div>
          <Button className="w-full" onClick={handleAddRoute} disabled={loading}>Add Navigator Route</Button>
        </CardContent>
      </Card>

      {result && (
        <Card className="md:col-span-2">
          <CardHeader className="py-3">
            <CardTitle className="text-sm font-medium">{result.type}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="p-4 bg-muted rounded-md overflow-auto max-h-60">
              <pre className="text-xs">
                {JSON.stringify(result.data, null, 2)}
              </pre>
            </div>
            <Button variant="ghost" size="sm" className="mt-2" onClick={() => setResult(null)}>Clear Result</Button>
          </CardContent>
        </Card>
      )}
    </div>
  );
}
