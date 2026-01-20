export interface Coordinates {
  id?: number;
  x: number;
  y: number;
}

export interface LocationFrom {
  id?: number;
  x: number;
  y: number;
  z: number;
  name?: string;
}

export interface LocationTo {
  id?: number;
  x: number;
  y: number;
  name?: string;
}

export interface Route {
  id: number;
  name: string;
  coordinates: Coordinates;
  creationDate: string;
  from?: LocationFrom;
  to?: LocationTo;
  distance: number;
}

export interface RouteRequest {
  name: string;
  coordinates: {
    x: number;
    y: number;
  };
  from?: {
    x: number;
    y: number;
    z: number;
    name?: string;
  };
  to?: {
    x: number;
    y: number;
    name?: string;
  };
  distance: number;
}

export interface RouteResponse {
  routes: Route[];
  total: number;
  page: number;
  pageSize: number;
  limit: number;
  offset?: number;
}

export interface ErrorResponse {
  code: string;
  message: string;
}
