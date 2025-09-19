package com.netra.commons.models.endpoint;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ProxyConfig {
    private String host;
    private Integer port;
    private String username;
    private String password; // always vault alias
    private ProxyType type = ProxyType.HTTP;

  public  enum ProxyType { HTTP, SOCKS }
}

