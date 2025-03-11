package org.example.payload.response;

public class NotificationCountResponse {

    private long unreadCount;
    private long totalCount;

    public NotificationCountResponse() {
    }

    public NotificationCountResponse(long unreadCount, long totalCount) {
        this.unreadCount = unreadCount;
        this.totalCount = totalCount;
    }

    // Getters and Setters
    public long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
