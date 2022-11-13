select count(1) from posts;

select * from posts where owneruserid = 1 AND creationdate > '2010-01-01' and creationdate < '2011-01-01';

explain analyse select * from posts where owneruserid = 4 AND creationdate > '2010-01-01' and creationdate < '2011-01-01';
explain analyse select * from posts where owneruserid = 31312432;

create index if not exists idx_owneruserid_on_posts on posts(owneruserid);
create index if not exists idx_owneruserid_creationdate_on_posts on posts(owneruserid, creationdate);

drop index if exists idx_owneruserid_creationdate_on_posts;
drop index if exists idx_owneruserid_on_posts;

drop index idx_creationdate_on_posts;

create index concurrently if not exists idx_creationdate_on_posts on posts(creationdate asc);

explain analyse select * from posts order by creationdate asc offset 5000000 limit 100;

select * from posts order by creationdate asc offset 5000000 limit 100;

explain analyse select * from posts where creationdate > '2015-05-24 09:23:23.93' order by creationdate asc FETCH FIRST 100 ROWS ONLY;