-- Admin queries
-- Index size query
SELECT indexname,
       pg_size_pretty(pg_relation_size(indexname::regclass)) AS index_size
FROM pg_indexes
WHERE tablename = 'posts';

-- Table size query
-- 51 GB
SELECT pg_size_pretty(pg_relation_size('posts')) AS table_size;

-- Column size query
-- 55 GB
SELECT pg_size_pretty(SUM(pg_column_size(t))) AS total_column_size
FROM posts AS t;

-- Column size for specific columns
-- 285 MB
SELECT 'body'                                      AS column_name,
       pg_size_pretty(SUM(pg_column_size('body'))) AS column_size
FROM posts;

-- List invalid indexes
SELECT indexrelid::regclass, indrelid::regclass, indisvalid
FROM pg_index
WHERE indisvalid IS FALSE;

-- List all the indexes for a specific table
SELECT *
FROM pg_indexes
WHERE tablename = 'posts';