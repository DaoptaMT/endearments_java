------------------------------------------------------------
-- ENUM TYPES
------------------------------------------------------------
CREATE TYPE message_type AS ENUM ('text', 'image', 'voice', 'music');
CREATE TYPE ticket_type AS ENUM ('text', 'image', 'voice', 'music');
CREATE TYPE approval_status AS ENUM ('pending','approved','rejected','resolved','closed');
CREATE TYPE channel_type AS ENUM ('email','sms','zalo','inapp');
CREATE TYPE log_status AS ENUM ('pending','sent','failed');
CREATE TYPE notify_status AS ENUM ('unread','read');
CREATE TYPE blog_status AS ENUM ('draft','published','archived');

------------------------------------------------------------
-- TRIGGER FUNCTION FOR updated_at
------------------------------------------------------------
CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = CURRENT_TIMESTAMP;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

------------------------------------------------------------
-- USERS
------------------------------------------------------------
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  provider VARCHAR(255),
  phone VARCHAR(255),
  password VARCHAR(255),
  avatar_url VARCHAR(255),
  bio TEXT,
  remember_token VARCHAR(100),
  otp VARCHAR(255),
  otp_expires_at TIMESTAMP,
  last_otp_sent_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_users
BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- ROLES
------------------------------------------------------------
CREATE TABLE roles (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255),
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_roles
BEFORE UPDATE ON roles
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- ROLE USER
------------------------------------------------------------
CREATE TABLE role_user (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users(id),
  role_id BIGINT REFERENCES roles(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_role_user
BEFORE UPDATE ON role_user
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- PERMISSIONS
------------------------------------------------------------
CREATE TABLE permissions (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255),
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_permissions
BEFORE UPDATE ON permissions
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- PERMISSION ROLE
------------------------------------------------------------
CREATE TABLE permission_role (
  id BIGSERIAL PRIMARY KEY,
  permission_id BIGINT REFERENCES permissions(id),
  role_id BIGINT REFERENCES roles(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_permission_role
BEFORE UPDATE ON permission_role
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- TICKET
------------------------------------------------------------
CREATE TABLE ticket (
  id BIGSERIAL PRIMARY KEY,

  sender_id BIGINT REFERENCES users(id),
  assigned_admin_id BIGINT REFERENCES users(id),

  subject VARCHAR(255),
  content TEXT,
  type ticket_type DEFAULT 'text',
  is_anonymous BOOLEAN DEFAULT FALSE,

  approval_status approval_status DEFAULT 'pending',
  rejection_reason TEXT,

  receiver_name VARCHAR(255),
  recipient_email VARCHAR(255),
  recipient_phone VARCHAR(20),
  recipient_zalo VARCHAR(255),

  private_note TEXT,
  sent_at TIMESTAMP,
  approved_at TIMESTAMP,
  status_changed_at TIMESTAMP,

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_ticket
BEFORE UPDATE ON ticket
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- MESSAGE
------------------------------------------------------------
CREATE TABLE message (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES ticket(id),
  user_id BIGINT REFERENCES users(id),
  content TEXT,
  type message_type DEFAULT 'text',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_message
BEFORE UPDATE ON message
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- ATTACHMENTS
------------------------------------------------------------
CREATE TABLE attachments (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users(id),
  message_id BIGINT REFERENCES message(id),

  type message_type NOT NULL,
  file_path VARCHAR(255),
  file_name VARCHAR(255),
  mime_type VARCHAR(255),
  duration INT,
  size BIGINT,

  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_attachments
BEFORE UPDATE ON attachments
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- MESSAGE ATTACHMENTS
------------------------------------------------------------
CREATE TABLE message_attachments (
  id BIGSERIAL PRIMARY KEY,
  message_id BIGINT REFERENCES message(id),
  attachment_id BIGINT REFERENCES attachments(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_message_attachments
BEFORE UPDATE ON message_attachments
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- MESSAGE LOGS
------------------------------------------------------------
CREATE TABLE message_logs (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES ticket(id),
  channel channel_type NOT NULL,
  status log_status DEFAULT 'pending',
  sent_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_message_logs
BEFORE UPDATE ON message_logs
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- TICKET STATUS HISTORIES
------------------------------------------------------------
CREATE TABLE ticket_status_histories (
  id BIGSERIAL PRIMARY KEY,
  ticket_id BIGINT REFERENCES ticket(id),
  changed_by BIGINT REFERENCES users(id),
  old_status VARCHAR(50),
  new_status VARCHAR(50),
  note TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

------------------------------------------------------------
-- NOTIFICATIONS
------------------------------------------------------------
CREATE TABLE notifications (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users(id),
  type VARCHAR(255),
  content TEXT,
  status notify_status DEFAULT 'unread',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_notifications
BEFORE UPDATE ON notifications
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- BLOGS
------------------------------------------------------------
CREATE TABLE blogs (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  content TEXT,
  author_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
  status blog_status DEFAULT 'draft',
  published_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_blogs
BEFORE UPDATE ON blogs
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- CATEGORY
------------------------------------------------------------
CREATE TABLE category (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_category
BEFORE UPDATE ON category
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

------------------------------------------------------------
-- CATEGORY BLOGS
------------------------------------------------------------
CREATE TABLE category_blogs (
  id BIGSERIAL PRIMARY KEY,
  blog_id BIGINT REFERENCES blogs(id) ON DELETE CASCADE,
  category_id BIGINT REFERENCES category(id) ON DELETE CASCADE
);

------------------------------------------------------------
-- BLOG ATTACHMENTS
------------------------------------------------------------
CREATE TABLE blog_attachments (
  id BIGSERIAL PRIMARY KEY,
  attachment_id BIGINT REFERENCES attachments(id),
  blog_id BIGINT REFERENCES blogs(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER set_timestamp_blog_attachments
BEFORE UPDATE ON blog_attachments
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();
