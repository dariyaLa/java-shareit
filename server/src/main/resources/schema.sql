drop table IF EXISTS users CASCADE;
create TABLE IF NOT EXISTS users (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(255) NOT NULL,
  email varchar(100) NOT NULL UNIQUE
);

drop table IF EXISTS requests CASCADE;
create TABLE IF NOT EXISTS requests (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  description varchar(255) NOT NULL,
  requestor int REFERENCES users (id),
  created timestamp without time zone NOT NULL,
  CONSTRAINT fk_requests_to_users FOREIGN KEY(requestor) REFERENCES users(id) ON DELETE CASCADE
  );


drop table IF EXISTS items CASCADE;
create TABLE IF NOT EXISTS items (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name varchar(255) NOT NULL,
  description varchar(255) NOT NULL,
  available boolean DEFAULT false,
  user_id int REFERENCES users (id),
  request_id int REFERENCES requests (id),

  CONSTRAINT fk_items_to_users FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_items_to_requests FOREIGN KEY(request_id) REFERENCES requests(id) ON DELETE CASCADE
);

drop table IF EXISTS comments CASCADE;
create TABLE IF NOT EXISTS comments (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  text varchar(255) NOT NULL,
  item_id int REFERENCES items (id),
  user_id int REFERENCES users (id),
  created timestamp without time zone NOT NULL,

  CONSTRAINT fk_comments_to_users FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_comments_to_items FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE
  );

drop table IF EXISTS booking CASCADE;
create TABLE IF NOT EXISTS booking (
  id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  start_date timestamp without time zone NOT NULL,
  end_date timestamp without time zone NOT NULL,
  item_id int REFERENCES items (id),
  user_id int REFERENCES users (id),
  state varchar(12) NOT NULL,

  CONSTRAINT fk_booking_to_items FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE,
  CONSTRAINT fk_booking_to_users FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE

);