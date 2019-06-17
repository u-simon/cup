package com.ca.ms.cup.common.cache.redis;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class JedisClusterFactoryBean implements FactoryBean<JedisCluster>, InitializingBean {
    private static final String nodeItemSplitter = ":";
    private static final String nodeSplitter = ",; \t\n";
    private JedisCluster jedisCluster;
    private int connectTimeout = 3000;
    private int socketTimeout = 5000;
    private int maxAttempts = 3;
    private GenericObjectPoolConfig poolConfig;
    private Set<HostAndPort> hostAndPorts;
    private String node;

    public void setConnectTimeout(int connectTimeout) {
        if (connectTimeout <= 0) {
            return;
        }
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        if (socketTimeout <= 0) {
            return;
        }
        this.socketTimeout = socketTimeout;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public void setNode(String node) {
        this.node = node;
    }

    @Override
    public JedisCluster getObject() throws Exception {
        return jedisCluster;
    }

    @Override
    public Class<?> getObjectType() {
        return JedisCluster.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        checkProperties();
        parseNode();
        initJedisCluster();
    }

    private void checkProperties() {
        Preconditions.checkArgument(StringUtils.isNotBlank(node), "node should be set!");
    }

    private void parseNode() {
        String[] nodeItemArray = StringUtils.split(node, nodeSplitter);
        Preconditions.checkArgument(nodeItemArray.length > 0, "no any node!");
        hostAndPorts = new HashSet<>(nodeItemArray.length);
        for (String nodeItem : nodeItemArray) {
            hostAndPorts.add(parse(nodeItem));
        }
    }

    private HostAndPort parse(String node) {
        Preconditions.checkArgument(StringUtils.isNotBlank(node), "node is blank!");
        String[] nodeParts = node.split(nodeItemSplitter);
        Preconditions.checkArgument(nodeParts.length == 2, "node is illegal!");
        return new HostAndPort(nodeParts[0], Integer.valueOf(nodeParts[1]));
    }

    private void initJedisCluster() {
        jedisCluster = new JedisCluster(hostAndPorts, connectTimeout, socketTimeout, maxAttempts, poolConfig);
    }

}
