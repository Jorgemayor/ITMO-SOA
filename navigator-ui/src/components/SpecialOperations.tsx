"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { getRouteWithMinimumName, getRoutesWithEqualDistance, getRoutesWithLessDistance } from "@/lib/api";
import type { Route } from "@/types";

export function SpecialOperations() {
  const [result, setResult] = useState<any>(null);
  const [distance, setDistance] = useState<string>("");

  const handleMinName = async () => {
    try {
      const res = await getRouteWithMinimumName();
      setResult({ type: "Minimum Name Route", data: res });
    } catch (e: any) {
      alert(e.message);
    }
  };

  const handleEqualDistance = async () => {
    try {
      const res = await getRoutesWithEqualDistance(Number(distance));
      setResult({ type: `Routes with Distance = ${distance}`, data: res.routes });
    } catch (e: any) {
      alert(e.message);
    }
  };

  const handleLessDistance = async () => {
    try {
      const res = await getRoutesWithLessDistance(Number(distance));
      setResult({ type: `Routes with Distance < ${distance}`, data: res.routes });
    } catch (e: any) {
      alert(e.message);
    }
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Special Operations</CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <div className="flex flex-wrap gap-2">
          <Button variant="outline" onClick={handleMinName}>Get Min Name Route</Button>
          
          <div className="flex gap-2">
            <Input 
              type="number" 
              placeholder="Distance" 
              value={distance} 
              onChange={(e) => setDistance(e.target.value)}
              className="w-32"
            />
            <Button variant="outline" onClick={handleEqualDistance} disabled={!distance}>Equal Distance</Button>
            <Button variant="outline" onClick={handleLessDistance} disabled={!distance}>Less Distance</Button>
          </div>
        </div>

        {result && (
          <div className="mt-4 p-4 bg-muted rounded-md overflow-auto max-h-60">
            <h4 className="font-bold mb-2">{result.type}</h4>
            <pre className="text-xs">
              {JSON.stringify(result.data, null, 2)}
            </pre>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
