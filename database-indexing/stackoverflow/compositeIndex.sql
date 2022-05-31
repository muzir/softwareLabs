select count(1) from posts;

select * from posts where owneruserid = 1 AND creationdate > '2010-01-01' and creationdate < '2011-01-01';

explain analyse select * from posts where owneruserid = 4 AND creationdate > '2010-01-01' and creationdate < '2011-01-01';
explain analyse select * from posts where owneruserid = 31312432;

create index if not exists idx_owneruserid_on_posts on posts(owneruserid);
create index if not exists idx_owneruserid_creationdate_on_posts on posts(owneruserid, creationdate);

drop index if exists idx_owneruserid_creationdate_on_posts;
drop index if exists idx_owneruserid_on_posts;