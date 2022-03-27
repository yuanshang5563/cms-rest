package org.ys.core.service;

import org.ys.core.model.CoreParameter;

/**
 * redis缓存操作
 */
public interface RedisService {
    /**
     * 系统初始化
     */
    public void initSystemCache() throws Exception;

    /**
     * 刷新缓存
     * @throws Exception
     */
    public void refreshSystemCache() throws Exception;

    /**
     * 刷新系统参数
     * @param coreParameter
     * @throws Exception
     */
    public void refreshCoreParameter(CoreParameter coreParameter) throws Exception;
}
