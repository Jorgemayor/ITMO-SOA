package soa.dto;

import soa.model.Route;
import java.util.List;

/**
 * DTO for paginated route responses.
 */
public class RouteResponse {
    
    private List<Route> routes;
    private Integer total;
    private Integer page;
    private Integer pageSize;
    private Integer limit;
    private Integer offset;
    
    public RouteResponse() {
    }
    
    public RouteResponse(List<Route> routes, Integer total, Integer page, Integer pageSize, Integer limit, Integer offset) {
        this.routes = routes;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.limit = limit;
        this.offset = offset;
    }
    
    public List<Route> getRoutes() {
        return routes;
    }
    
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
    
    public Integer getTotal() {
        return total;
    }
    
    public void setTotal(Integer total) {
        this.total = total;
    }
    
    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    public Integer getOffset() {
        return offset;
    }
    
    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}

