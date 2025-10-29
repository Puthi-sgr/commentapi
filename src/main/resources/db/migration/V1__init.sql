-- V1: base schema for notes + threaded comments
CREATE TABLE note (
  id            BIGSERIAL PRIMARY KEY,
  title         TEXT        NOT NULL,
  content       TEXT,
  owner_id      BIGINT      NOT NULL,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  comment_count INT         NOT NULL DEFAULT 0
);

CREATE TABLE comment (
  id         BIGSERIAL PRIMARY KEY,
  note_id    BIGINT      NOT NULL,
  parent_id  BIGINT,
  content    TEXT        NOT NULL,
  author_id  BIGINT      NOT NULL,
  depth      INT         NOT NULL DEFAULT 0,
  path       TEXT,                 -- optional; we may enforce NOT NULL later
  deleted    BOOLEAN     NOT NULL DEFAULT FALSE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT fk_comment_note
    FOREIGN KEY (note_id) REFERENCES note(id) ON DELETE CASCADE
);

-- make (note_id,id) uniquely referenceable
CREATE UNIQUE INDEX ux_comment_note_id_id ON comment(note_id, id);

-- composite FK: ensures parent & child are in the SAME note
ALTER TABLE comment
ADD CONSTRAINT fk_comment_parent_same_note
FOREIGN KEY (note_id, parent_id)
REFERENCES comment (note_id, id)
ON DELETE CASCADE;

-- helpful indexes
CREATE INDEX idx_comment_note_parent ON comment(note_id, parent_id);
CREATE INDEX idx_comment_parent      ON comment(parent_id);
CREATE INDEX idx_comment_note_path   ON comment(note_id, path);
