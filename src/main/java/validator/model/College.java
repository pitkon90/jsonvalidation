package validator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author Balázs Ludrik
 */
@JsonPropertyOrder(value = {"webPages", "name", "alphaTwoCode", "stateProvince", "domains" })
@JsonRootName("college")
public class College implements Serializable {

    @JsonProperty
    private String[] webPages;

    @JsonProperty
    private Optional<String> name;

    @JsonProperty
    private Optional<String> alphaTwoCode;

    @JsonProperty
    private Optional<String> stateProvince;

    @JsonProperty
    private String[] domains;

    @JsonProperty
    private Optional<String> country;

    public String[] getWebPages() {
        return webPages;
    }

    public void setWebPages(String[] webPages) {
        this.webPages = webPages;
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getAlphaTwoCode() {
        return alphaTwoCode;
    }

    public void setAlphaTwoCode(Optional<String> alphaTwoCode) {
        this.alphaTwoCode = alphaTwoCode;
    }

    public Optional<String> getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(Optional<String> stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String[] getDomains() {
        return domains;
    }

    public void setDomains(String[] domains) {
        this.domains = domains;
    }

    public Optional<String> getCountry() {
        return country;
    }

    public void setCountry(Optional<String> country) {
        this.country = country;
    }
}
