package com.ca.ms.cup.common.task.execute;


import com.ca.ms.cup.common.task.execute.impl.TaskDefinitionConfig;

import java.util.Collection;

/**
 *
 */
public interface TaskDefinitionConfigSource {

    TaskDefinitionConfig get(Integer bizType);

    String getTableName(Integer bizType);

    Collection<String> getAllTableNames();
}
