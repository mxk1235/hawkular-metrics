/*
 * Copyright 2014-2015 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.metrics.core.impl.cassandra;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import org.hawkular.metrics.core.api.Availability;
import org.hawkular.metrics.core.api.AvailabilityData;
import org.hawkular.metrics.core.api.Counter;
import org.hawkular.metrics.core.api.Gauge;
import org.hawkular.metrics.core.api.GaugeData;
import org.hawkular.metrics.core.api.Interval;
import org.hawkular.metrics.core.api.Metric;
import org.hawkular.metrics.core.api.MetricData;
import org.hawkular.metrics.core.api.MetricId;
import org.hawkular.metrics.core.api.MetricType;
import org.hawkular.metrics.core.api.Retention;
import org.hawkular.metrics.core.api.Tenant;
import rx.Observable;

/**
 * @author John Sanda
 */
public class DelegatingDataAccess implements DataAccess {

    private final DataAccess delegate;

    public DelegatingDataAccess(DataAccess delegate) {
        this.delegate = delegate;
    }

    @Override
    public ResultSetFuture insertTenant(Tenant tenant) {
        return delegate.insertTenant(tenant);
    }

    @Override
    public ResultSetFuture findAllTenantIds() {
        return delegate.findAllTenantIds();
    }

    @Override
    public ResultSetFuture findTenant(String id) {
        return delegate.findTenant(id);
    }

    @Override
    public ResultSetFuture insertMetricInMetricsIndex(Metric metric) {
        return delegate.insertMetricInMetricsIndex(metric);
    }

    @Override
    public Observable<ResultSet> findMetric(String tenantId, MetricType type, MetricId id, long dpart) {
        return delegate.findMetric(tenantId, type, id, dpart);
    }

    @Override
    public ResultSetFuture getMetricTags(String tenantId, MetricType type, MetricId id, long dpart) {
        return delegate.getMetricTags(tenantId, type, id, dpart);
    }

    @Override
    public ResultSetFuture addTagsAndDataRetention(Metric metric) {
        return delegate.addTagsAndDataRetention(metric);
    }

    @Override
    public ResultSetFuture addTags(Metric<?> metric, Map<String, String> tags) {
        return delegate.addTags(metric, tags);
    }

    @Override
    public ResultSetFuture deleteTags(Metric<?> metric, Set<String> tags) {
        return delegate.deleteTags(metric, tags);
    }

    @Override
    public ResultSetFuture updateTagsInMetricsIndex(Metric<?> metric, Map<String, String> additions,
        Set<String> deletions) {
        return delegate.updateTagsInMetricsIndex(metric, additions, deletions);
    }

    @Override
    public <T extends Metric<?>> ResultSetFuture updateMetricsIndex(List<T> metrics) {
        return delegate.updateMetricsIndex(metrics);
    }

    @Override
    public Observable<ResultSet> findMetricsInMetricsIndex(String tenantId, MetricType type) {
        return delegate.findMetricsInMetricsIndex(tenantId, type);
    }

    @Override
    public ResultSetFuture insertData(Gauge metric, int ttl) {
        return delegate.insertData(metric, ttl);
    }

    @Override
    public Observable<ResultSet> findData(String tenantId, MetricId id, long startTime, long endTime) {
        return delegate.findData(tenantId, id, startTime, endTime);
    }

    @Override
    public ResultSetFuture findData(Gauge metric, long startTime, long endTime, Order order) {
        return delegate.findData(metric, startTime, endTime, order);
    }

    @Override
    public Observable<ResultSet> findData(String tenantId, MetricId id, long startTime, long endTime,
            boolean includeWriteTime) {
        return delegate.findData(tenantId, id, startTime, endTime, includeWriteTime);
    }

    @Override
    public ResultSetFuture findData(Gauge metric, long timestamp, boolean includeWriteTime) {
        return delegate.findData(metric, timestamp, includeWriteTime);
    }

    @Override
    public ResultSetFuture findData(Availability metric, long startTime, long endTime) {
        return delegate.findData(metric, startTime, endTime);
    }

    @Override
    public ResultSetFuture findData(Availability metric, long startTime, long endTime, boolean includeWriteTime) {
        return delegate.findData(metric, startTime, endTime, includeWriteTime);
    }

    @Override
    public ResultSetFuture findData(Availability metric, long timestamp) {
        return delegate.findData(metric, timestamp);
    }

    @Override
    public ResultSetFuture deleteGuageMetric(String tenantId, String metric, Interval interval, long dpart) {
        return delegate.deleteGuageMetric(tenantId, metric, interval, dpart);
    }

    @Override
    public ResultSetFuture findAllGuageMetrics() {
        return delegate.findAllGuageMetrics();
    }

    @Override
    public ResultSetFuture insertGuageTag(String tag, String tagValue, Gauge metric,
            List<GaugeData> data) {
        return delegate.insertGuageTag(tag, tagValue, metric, data);
    }

    @Override
    public ResultSetFuture insertAvailabilityTag(String tag, String tagValue, Availability metric,
            List<AvailabilityData> data) {
        return delegate.insertAvailabilityTag(tag, tagValue, metric, data);
    }

    @Override
    public ResultSetFuture updateDataWithTag(Metric<?> metric, MetricData data, Map<String, String> tags) {
        return delegate.updateDataWithTag(metric, data, tags);
    }

    @Override
    public ResultSetFuture findGuageDataByTag(String tenantId, String tag, String tagValue) {
        return delegate.findGuageDataByTag(tenantId, tag, tagValue);
    }

    @Override
    public ResultSetFuture findAvailabilityByTag(String tenantId, String tag, String tagValue) {
        return delegate.findAvailabilityByTag(tenantId, tag, tagValue);
    }

    @Override
    public ResultSetFuture insertData(Availability metric, int ttl) {
        return delegate.insertData(metric, ttl);
    }

    @Override
    public ResultSetFuture findAvailabilityData(String tenantId, MetricId id, long startTime, long endTime) {
        return delegate.findAvailabilityData(tenantId, id, startTime, endTime);
    }

    @Override
    public ResultSetFuture updateCounter(Counter counter) {
        return delegate.updateCounter(counter);
    }

    @Override
    public ResultSetFuture updateCounters(Collection<Counter> counters) {
        return delegate.updateCounters(counters);
    }

    @Override
    public ResultSetFuture findDataRetentions(String tenantId, MetricType type) {
        return delegate.findDataRetentions(tenantId, type);
    }

    @Override
    public ResultSetFuture updateRetentionsIndex(String tenantId, MetricType type, Set<Retention> retentions) {
        return delegate.updateRetentionsIndex(tenantId, type, retentions);
    }

    @Override
    public ResultSetFuture updateRetentionsIndex(Metric metric) {
        return delegate.updateRetentionsIndex(metric);
    }

    @Override
    public ResultSetFuture insertIntoMetricsTagsIndex(Metric<?> metric, Map<String, String> tags) {
        return delegate.insertIntoMetricsTagsIndex(metric, tags);
    }

    @Override
    public ResultSetFuture deleteFromMetricsTagsIndex(Metric<?> metric, Map<String, String> tags) {
        return delegate.deleteFromMetricsTagsIndex(metric, tags);
    }

    @Override
    public ResultSetFuture findMetricsByTag(String tenantId, String tag) {
        return delegate.findMetricsByTag(tenantId, tag);
    }
}
