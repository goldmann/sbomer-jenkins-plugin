package org.sbomer.jenkins;

import java.io.IOException;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import jakarta.servlet.ServletException;
import jenkins.tasks.SimpleBuildStep;

public class RequestGenerationStep extends Builder implements SimpleBuildStep {
    private String identifier;
    private String type;

    @DataBoundConstructor
    public RequestGenerationStep(String identifier, String type) {
        this.identifier = identifier;
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }

    @DataBoundSetter
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @DataBoundSetter
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, EnvVars env, Launcher launcher, TaskListener listener)
            throws InterruptedException, IOException {
        listener.getLogger().println(
                "Requesting generation of artifact with identifier: " + identifier + " and type: " + type);
    }

    @Symbol("sbomerGenerateManifests")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation validate(@QueryParameter String identifier, @QueryParameter String type)
                throws IOException, ServletException {

            if (identifier.length() == 0)
                return FormValidation.error(Messages.RequestGenerationStep_DescriptorImpl_errors_missingIdentifier());

            if (type.length() == 0)
                return FormValidation.error(Messages.RequestGenerationStep_DescriptorImpl_errors_missingType());

            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.RequestGenerationStep_DescriptorImpl_DisplayName();
        }
    }
}
