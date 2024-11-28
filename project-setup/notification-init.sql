CREATE SCHEMA IF NOT EXISTS notification;

CREATE TABLE IF NOT EXISTS notification.user_notification_details (
    und_id UUID PRIMARY KEY,
    und_email VARCHAR(255),
    und_phone VARCHAR(20)
);

