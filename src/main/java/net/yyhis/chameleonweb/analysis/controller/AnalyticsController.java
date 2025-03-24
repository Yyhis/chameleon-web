package net.yyhis.chameleonweb.analysis.controller;

import com.google.analytics.data.v1beta.*;
import com.google.auth.oauth2.GoogleCredentials;

import lombok.extern.slf4j.Slf4j;
import net.yyhis.chameleonweb.config.GA4Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/v1/analysis")
@Slf4j 
public class AnalyticsController {

    @Autowired
    private GA4Config ga4Config;

    @GetMapping("/ga4-report")
    public String generateReport() {
        String relativePath = ga4Config.getCredentialsPath(); // 상대 경로
        Path path = Paths.get(System.getProperty("user.dir"), relativePath);

        try {
            // GoogleCredentials 생성
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream(path.toFile()))
                    .createScoped("https://www.googleapis.com/auth/analytics.readonly");

            // BetaAnalyticsDataSettings 설정
            BetaAnalyticsDataSettings settings = BetaAnalyticsDataSettings.newBuilder()
                    .setCredentialsProvider(() -> credentials)
                    .build();

            // BetaAnalyticsDataClient 생성
            try (BetaAnalyticsDataClient analyticsDataClient = BetaAnalyticsDataClient.create(settings)) {
                // 요청 생성
                log.info("GA4 Property ID: {}", ga4Config.getPropertyId());

                RunReportRequest request = RunReportRequest.newBuilder()
                        .setProperty("properties/" + ga4Config.getPropertyId())
                        .addDimensions(Dimension.newBuilder().setName("city")) // 도시별 데이터를 수집
                        .addMetrics(Metric.newBuilder().setName("activeUsers")) // 활성 사용자 수
                        .addDateRanges(DateRange.newBuilder().setStartDate("2023-01-01").setEndDate("today")) // 기간 설정
                        .build();

                // 보고서 실행
                RunReportResponse response = analyticsDataClient.runReport(request);

                // 결과 출력
                StringBuilder result = new StringBuilder();
                result.append("GA4 Report:\n");
                for (Row row : response.getRowsList()) {
                    result.append("City: ")
                          .append(row.getDimensionValues(0).getValue())
                          .append(", Active Users: ")
                          .append(row.getMetricValues(0).getValue())
                          .append("\n");
                }
                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating report: " + e.getMessage();
        }
    }
}

