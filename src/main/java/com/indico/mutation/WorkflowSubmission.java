package com.indico.mutation;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Response;
import com.indico.*;
import com.indico.storage.UploadFile;
import com.indico.type.FileInput;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkflowSubmission implements Mutation<List<Integer>> {
    private IndicoClient client;
    private int workflowId;
    private List<String> files;
    private List<String> urls;

    public WorkflowSubmission(IndicoClient client) {
        this.client = client;
    }

    public WorkflowSubmission workflowId(int workflowId) {
        this.workflowId = workflowId;
        return this;
    }

    public WorkflowSubmission files(List<String> files) {
        this.files = files;
        return this;
    }

    public WorkflowSubmission urls(List<String> urls) {
        this.urls = urls;
        return this;
    }

    @Override
    public List<Integer> execute() {
        if (this.files == null && this.urls == null) {
            throw new RuntimeException("One of 'files' or 'urls' must be specified");
        } else if (this.files != null && this.urls != null) {
            throw new RuntimeException("Only one of 'files' or 'urls' must be specified");
        }

        if (this.files != null) {
            List<FileInput> files = new ArrayList<>();

            try {
                JSONArray fileMetadata = this.upload(this.files);
                for (Object f : fileMetadata) {
                    JSONObject uploadMeta = (JSONObject) f;
                    JSONObject meta = new JSONObject();
                    meta.put("name", uploadMeta.getString("name"));
                    meta.put("path", uploadMeta.getString("path"));
                    meta.put("upload_type", uploadMeta.getString("upload_type"));
                    FileInput input = FileInput.builder().filename(((JSONObject) f).getString("name")).filemeta(meta).build();
                    files.add(input);
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e.fillInStackTrace());
            }

            ApolloCall<WorkflowSubmissionGraphQLMutation.Data> apolloCall = this.client.apolloClient.mutate(WorkflowSubmissionGraphQLMutation.builder()
                    .files(files)
                    .workflowId(this.workflowId)
                    .build());

            Response<WorkflowSubmissionGraphQLMutation.Data> response = Async.executeSync(apolloCall).join();
            if (response.hasErrors()) {
                StringBuilder errors = new StringBuilder();
                for (Error err : response.errors()) {
                    errors.append(err.toString() + "\n");
                }
                String msg = errors.toString();
                throw new RuntimeException("Failed to extract documents due to following error: \n" + msg);
            }

            return response.data().workflowSubmission().submissionIds();
        } else {
            ApolloCall<WorkflowSubmissionUrlGraphQLMutation.Data> apolloCall = this.client.apolloClient.mutate(WorkflowSubmissionUrlGraphQLMutation.builder()
                    .urls(this.urls)
                    .workflowId(this.workflowId)
                    .build());

            Response<WorkflowSubmissionUrlGraphQLMutation.Data> response = Async.executeSync(apolloCall).join();
            if (response.hasErrors()) {
                StringBuilder errors = new StringBuilder();
                for (Error err : response.errors()) {
                    errors.append(err.toString() + "\n");
                }
                String msg = errors.toString();
                throw new RuntimeException("Failed to extract documents due to following error: \n" + msg);
            }

            return response.data().workflowUrlSubmission().submissionIds();
        }
    }

    private JSONArray upload(List<String> filePaths) throws IOException {
        UploadFile uploadRequest = new UploadFile(this.client);
        return uploadRequest.filePaths(filePaths).call();
    }
}
