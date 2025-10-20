package org.sbomer.jenkins;

import com.cloudbees.plugins.credentials.common.AbstractIdCredentialsListBoxModel;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import hudson.Extension;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.util.ListBoxModel;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest2;

@Extension
public class SbomerConfiguration extends GlobalConfiguration {
    /**
     * A static method to easily retrieve the global configuration instance.
     *
     * @return The singleton instance of this global configuration.
     */
    public static SbomerConfiguration get() {
        return GlobalConfiguration.all().get(SbomerConfiguration.class);
    }

    /**
     * Location of the SBOMer API.
     */
    private String apiUrl;

    /**
     * The API token. Stored as a Secret to ensure it's encrypted on disk.
     */
    private String apiTokenId;

    /**
     * The constructor loads the configuration from disk when Jenkins starts.
     */
    public SbomerConfiguration() {
        load();
    }

    /**
     * Getter for the API token. This is used by the Jelly script to populate the
     * form field.
     *
     * @return The encrypted API token.
     */
    public String getApiTokenId() {
        return apiTokenId;
    }

    /**
     * Retrieve the API URL. This is used by the Jelly script to populate the
     * form field.
     *
     * @return URL of the SBOMer API.
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * Setter for the API token.
     *
     * @param apiToken The new API token to set.
     */
    @DataBoundSetter
    public void setApiTokenId(String apiTokenId) {
        this.apiTokenId = apiTokenId;
    }

    /**
     * Setter for the API URL.
     *
     * @param apiToken The new API token to set.
     */
    @DataBoundSetter
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * This method is called when the user clicks "Save" on the "Configure System"
     * page.
     *
     * @param req      The web request.
     * @param formData The form data submitted by the user as a JSON object.
     * @return true if the configuration was successfully processed.
     * @throws FormException if there is an error processing the form.
     */
    @Override
    public boolean configure(StaplerRequest2 req, JSONObject formData) throws FormException {
        // Bind the form data to this instance. Jenkins maps the form fields
        // to the setter methods automatically.
        req.bindJSON(this, formData);
        // Persist the configuration to disk.
        save();
        return super.configure(req, formData);
    }

    public ListBoxModel doFillApiTokenIdItems(@AncestorInPath Item item) {
        // TODO: Validate

        // Filter the list to only include a specific type
        AbstractIdCredentialsListBoxModel<StandardListBoxModel, StandardCredentials> list =
                new StandardListBoxModel().includeEmptyValue().includeAs(ACL.SYSTEM2, item, StringCredentials.class);

        return list;
    }

    /**
     * The display name that appears in the "Configure System" page.
     *
     * @return A descriptive name for your plugin's configuration section.
     */
    @Override
    public String getDisplayName() {
        return "SBOMer Configuration";
    }
}
