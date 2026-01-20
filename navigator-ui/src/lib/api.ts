import type { Route, RouteRequest, RouteResponse } from "@/types";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/navigator/api/v1";

export async function fetchRoutes(params: {
  page?: number;
  pageSize?: number;
  sort?: string[];
  sortDirection?: "ASC" | "DESC";
  filter?: string[];
}): Promise<RouteResponse> {
  const query = new URLSearchParams();
  if (params.page !== undefined) query.append("page", params.page.toString());
  if (params.pageSize !== undefined) query.append("pageSize", params.pageSize.toString());
  if (params.sort) params.sort.forEach(s => query.append("sort", s));
  if (params.sortDirection) query.append("sortDirection", params.sortDirection);
  if (params.filter) params.filter.forEach(f => query.append("filter", f));

  const response = await fetch(`${API_BASE_URL}/routes?${query.toString()}`);
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to fetch routes");
  }
  return response.json();
}

export async function createRoute(route: RouteRequest): Promise<Route> {
  const response = await fetch(`${API_BASE_URL}/route`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(route),
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to create route");
  }
  return response.json();
}

export async function updateRoute(id: number, route: Partial<RouteRequest>): Promise<Route> {
  const response = await fetch(`${API_BASE_URL}/routes/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(route),
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to update route");
  }
  return response.json();
}

export async function deleteRoute(id: number): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/routes/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to delete route");
  }
}

export async function getRouteWithMinimumName(): Promise<Route> {
  const response = await fetch(`${API_BASE_URL}/route/minimum-name`);
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to get route with minimum name");
  }
  return response.json();
}

export async function getRoutesWithEqualDistance(distance: number): Promise<RouteResponse> {
  const response = await fetch(`${API_BASE_URL}/routes/equal-distance/${distance}`);
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to get routes with equal distance");
  }
  return response.json();
}

export async function getRoutesWithLessDistance(distance: number): Promise<RouteResponse> {
  const response = await fetch(`${API_BASE_URL}/routes/less-distance/${distance}`);
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to get routes with less distance");
  }
  return response.json();
}
