package duckie.example.backend.service;

import duckie.example.backend.dto.DashboardStatsResponse;

public interface AdminDashboardService {
    DashboardStatsResponse getDashboardStats();
}