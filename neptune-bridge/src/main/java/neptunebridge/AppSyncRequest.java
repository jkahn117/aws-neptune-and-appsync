package neptunebridge;

import java.util.Map;

public class AppSyncRequest {
  private String query;
  private Map<String,Object> variables;

  public String getQuery() {
    return this.query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public Map<String,Object> getVariables() {
    return this.variables;
  }

  public void setVariables(Map<String,Object> variables) {
    this.variables = variables;
  }

  public boolean isValid() {
    return (this.query != null && !this.query.isEmpty());
  }
}