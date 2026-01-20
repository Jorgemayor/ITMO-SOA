"use client";

import { useState } from "react";
import { RouteList } from "@/components/RouteList";
import { RouteForm } from "@/components/RouteForm";
import { SpecialOperations } from "@/components/SpecialOperations";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";
import type { Route } from "@/types";

export default function Home() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingRoute, setEditingRoute] = useState<Route | undefined>(undefined);
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const handleCreate = () => {
    setEditingRoute(undefined);
    setIsFormOpen(true);
  };

  const handleEdit = (route: Route) => {
    setEditingRoute(route);
    setIsFormOpen(true);
  };

  const handleFormSuccess = () => {
    setIsFormOpen(false);
    setRefreshTrigger(prev => prev + 1);
  };

  return (
    <main className="container mx-auto py-10 space-y-8 px-4">
      <div className="flex justify-between items-center">
        <h1 className="text-4xl font-bold tracking-tight text-black dark:text-zinc-50">Navigator API Client</h1>
        {!isFormOpen && (
          <Button onClick={handleCreate}>
            <Plus className="mr-2 h-4 w-4" /> New Route
          </Button>
        )}
      </div>

      {isFormOpen ? (
        <RouteForm
          initialData={editingRoute}
          onSuccess={handleFormSuccess}
          onCancel={() => setIsFormOpen(false)}
        />
      ) : (
        <div className="space-y-8">
          <SpecialOperations />
          <div className="space-y-4">
            <h2 className="text-2xl font-semibold">Routes</h2>
            <RouteList onEdit={handleEdit} refreshTrigger={refreshTrigger} />
          </div>
        </div>
      )}
    </main>
  );
}
