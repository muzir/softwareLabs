select count(1) from posts;

select * from posts limit 10;

select * from posts where owneruserid = 1 AND creationdate between '2007-01-01' and '2008-01-01';

select * from posts where owneruserid = 1 AND creationdate > '2007-01-01' and creationdate < '2008-01-01';

select * from posts where owneruserid = 1 AND creationdate > '2010-01-01' and creationdate < '2011-01-01';

explain analyse select * from posts where owneruserid = 2 AND creationdate > '2010-01-01' and creationdate < '2011-01-01';

create index if not exists idx_owneruserid_creationdate_on_posts on posts(owneruserid, creationdate);