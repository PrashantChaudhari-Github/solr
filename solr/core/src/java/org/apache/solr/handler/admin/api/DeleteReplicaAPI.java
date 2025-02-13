/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.handler.admin.api;

import org.apache.solr.api.EndPoint;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.params.CollectionParams;
import org.apache.solr.handler.admin.CollectionsHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

import static org.apache.solr.client.solrj.SolrRequest.METHOD.DELETE;
import static org.apache.solr.common.params.CommonParams.ACTION;
import static org.apache.solr.common.params.CoreAdminParams.REPLICA;
import static org.apache.solr.common.params.CoreAdminParams.SHARD;
import static org.apache.solr.handler.ClusterAPI.wrapParams;
import static org.apache.solr.security.PermissionNameProvider.Name.COLL_EDIT_PERM;

/**
 * V2 API for deleting an existing replica from a shard.
 *
 * This API (DELETE /v2/collections/collectionName/shards/shardName/replicaName is analogous to the v1
 * /admin/collections?action=DELETEREPLICA command.
 */
public class DeleteReplicaAPI {

    private final CollectionsHandler collectionsHandler;

    public DeleteReplicaAPI(CollectionsHandler collectionsHandler) {
        this.collectionsHandler = collectionsHandler;
    }

    @EndPoint(
            path = {"/c/{collection}/shards/{shard}/{replica}", "/collections/{collection}/shards/{shard}/{replica}"},
            method = DELETE,
            permission = COLL_EDIT_PERM)
    public void deleteReplica(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
        req = wrapParams(req,
                ACTION, CollectionParams.CollectionAction.DELETEREPLICA.toString(),
                ZkStateReader.COLLECTION_PROP, req.getPathTemplateValues().get(ZkStateReader.COLLECTION_PROP),
                SHARD, req.getPathTemplateValues().get(SHARD),
                REPLICA, req.getPathTemplateValues().get(REPLICA));

        collectionsHandler.handleRequestBody(req, rsp);
    }
}
