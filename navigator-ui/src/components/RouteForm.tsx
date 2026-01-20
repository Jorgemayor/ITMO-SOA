"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { createRoute, updateRoute } from "@/lib/api";
import type { Route, RouteRequest } from "@/types";
import { toast } from "sonner";

interface RouteFormProps {
  initialData?: Route;
  onSuccess: () => void;
  onCancel: () => void;
}

export function RouteForm({ initialData, onSuccess, onCancel }: RouteFormProps) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    const formData = new FormData(e.currentTarget);
    const data: RouteRequest = {
      name: formData.get("name") as string,
      coordinates: {
        x: Number(formData.get("coordX")),
        y: Number(formData.get("coordY")),
      },
      distance: Number(formData.get("distance")),
    };

    const fromX = formData.get("fromX");
    const fromY = formData.get("fromY");
    const fromZ = formData.get("fromZ");
    const fromName = formData.get("fromName");

    if (fromX && fromY && fromZ) {
      data.from = {
        x: Number(fromX),
        y: Number(fromY),
        z: Number(fromZ),
        name: fromName as string || undefined,
      };
    }

    const toX = formData.get("toX");
    const toY = formData.get("toY");
    const toZ = formData.get("toZ");
    const toName = formData.get("toName");

    if (toX && toY && toZ) {
      data.to = {
        x: Number(toX),
        y: Number(toY),
        z: Number(toZ),
        name: toName as string || undefined,
      };
    }

    try {
      if (initialData) {
        await updateRoute(initialData.id, data);
        toast.success("Route updated successfully");
      } else {
        await createRoute(data);
        toast.success("Route created successfully");
      }
      onSuccess();
    } catch (err: any) {
      const message = err.message || "An error occurred";
      setError(message);
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card className="w-full max-w-2xl mx-auto">
      <CardHeader>
        <CardTitle>{initialData ? "Edit Route" : "Create New Route"}</CardTitle>
      </CardHeader>
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-4">
          {error && <div className="text-destructive text-sm font-medium">{error}</div>}
          
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="name">Route Name</Label>
              <Input id="name" name="name" defaultValue={initialData?.name} required />
            </div>
            <div className="space-y-2">
              <Label htmlFor="distance">Distance</Label>
              <Input id="distance" name="distance" type="number" defaultValue={initialData?.distance} required min="2" />
            </div>
          </div>

          <div className="space-y-2">
            <Label>Coordinates</Label>
            <div className="grid grid-cols-2 gap-4">
              <Input name="coordX" type="number" placeholder="X" defaultValue={initialData?.coordinates.x} required step="any" />
              <Input name="coordY" type="number" placeholder="Y" defaultValue={initialData?.coordinates.y} required step="any" />
            </div>
          </div>

          <div className="space-y-2">
            <Label>From Location (Optional)</Label>
            <div className="grid grid-cols-3 gap-2">
              <Input name="fromX" type="number" placeholder="X" defaultValue={initialData?.from?.x} step="any" />
              <Input name="fromY" type="number" placeholder="Y" defaultValue={initialData?.from?.y} step="any" />
              <Input name="fromZ" type="number" placeholder="Z" defaultValue={initialData?.from?.z} step="any" />
            </div>
            <Input name="fromName" placeholder="Location Name" defaultValue={initialData?.from?.name} />
          </div>

          <div className="space-y-2">
            <Label>To Location (Optional)</Label>
            <div className="grid grid-cols-3 gap-2">
              <Input name="toX" type="number" placeholder="X" defaultValue={initialData?.to?.x} step="any" />
              <Input name="toY" type="number" placeholder="Y" defaultValue={initialData?.to?.y} step="any" />
              <Input name="toZ" type="number" placeholder="Z" defaultValue={initialData?.to?.z} step="any" />
            </div>
            <Input name="toName" placeholder="Location Name" defaultValue={initialData?.to?.name} />
          </div>
        </CardContent>
        <CardFooter className="flex justify-end gap-2">
          <Button type="button" variant="outline" onClick={onCancel}>Cancel</Button>
          <Button type="submit" disabled={loading}>
            {loading ? "Saving..." : (initialData ? "Update Route" : "Create Route")}
          </Button>
        </CardFooter>
      </form>
    </Card>
  );
}
