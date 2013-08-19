/**
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.wennsoft.portlet.component;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.portal.webui.portal.UIPortalComponentActionListener.ViewChildActionListener;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * Created by The eXo Platform SARL Author : chungnv nguyenchung136@yahoo.com Jun 23, 2006 10:07:15 AM
 */

@ComponentConfig(lifecycle = UIApplicationLifecycle.class, template = "app:/groovy/webui/component/UIKeyEntitiesManagementPortlet.gtmpl", events = {
        @EventConfig(listeners = UIKeyEntitiesManagementPortlet.NewAccountAddedActionListener.class),
        @EventConfig(listeners = ViewChildActionListener.class) }

)
@Serialized
public class UIKeyEntitiesManagementPortlet extends UIPortletApplication {

    public UIKeyEntitiesManagementPortlet() throws Exception {

        addChild(UIUserManagement.class, null, null);

    }


    public static class NewAccountAddedActionListener extends EventListener<UIKeyEntitiesManagementPortlet> {
        public void execute(Event<UIKeyEntitiesManagementPortlet> event) throws Exception {

        }
    }
}
