package com.hyrc.lrs.xunsi.activity.rongIM;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;
import io.rong.sight.SightPlugin;

/**
 * @description 作用:
 * @date: 2020/3/31
 * @author: 卢融霜
 */
public class MyExtensionModule extends DefaultExtensionModule {
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        IPluginModule sightPlugin = new SightPlugin();
        pluginModules.add(sightPlugin);
        return pluginModules;
    }
}