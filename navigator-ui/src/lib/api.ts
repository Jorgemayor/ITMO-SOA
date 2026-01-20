import type { Route, RouteRequest, RouteResponse } from "@/types";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api/v1";

const handleResponse = async (response: Response) => {
  if (!response.ok) {
    let message = response.statusText;
    try {
      const error = await response.json();
      message = error.message || message;
    } catch (_) {
      // Keep statusText if json parsing fails
    }
    throw new Error(message || "Request failed");
  }
  return response.json();
};

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
  return handleResponse(response);
}

export async function createRoute(route: RouteRequest): Promise<Route> {
  const response = await fetch(`${API_BASE_URL}/route`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(route),
  });
  return handleResponse(response);
}

export async function updateRoute(id: number, route: Partial<RouteRequest>): Promise<Route> {
  const response = await fetch(`${API_BASE_URL}/routes/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(route),
  });
  return handleResponse(response);
}

export async function deleteRoute(id: number): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/routes/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) {
    let message = response.statusText;
    try {
      const error = await response.json();
      message = error.message || message;
    } catch (_) {
    }
    throw new Error(message || "Failed to delete route");
  }
}

export async function getRouteWithMinimumName(): Promise<Route> {
  const response = await fetch(`${API_BASE_URL}/route/minimum-name`);
  return handleResponse(response);
}

export async function getRoutesWithEqualDistance(distance: number): Promise<RouteResponse> {
  const response = await fetch(`${API_BASE_URL}/routes/equal-distance/${distance}`);
  return handleResponse(response);
}

export async function getRoutesWithLessDistance(distance: number): Promise<RouteResponse> {
  const response = await fetch(`${API_BASE_URL}/routes/less-distance/${distance}`);
  return handleResponse(response);
}
