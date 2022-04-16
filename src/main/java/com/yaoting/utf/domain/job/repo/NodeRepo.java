package com.yaoting.utf.domain.job.repo;

import com.yaoting.utf.domain.job.nm.Node;

import java.util.List;

public interface NodeRepo {

    boolean save(Node node);

    void update(Node node);

    Node queryByHostname(String hostname);

    List<Node> listAll();

    void delete(Node node);
}
