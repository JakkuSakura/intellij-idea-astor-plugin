package org.bytecamp.program_repair.astor_plugin.window

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager

object AstorNotification {
    fun getNotificationGroup(): NotificationGroup {
        return NotificationGroupManager.getInstance().getNotificationGroup("AstorDoneNotificationGroup")
    }
}