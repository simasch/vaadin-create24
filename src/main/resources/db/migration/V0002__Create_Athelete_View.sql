create view athlete_view as
select a.id, a.first_name, a.last_name, c.abbreviation || ' ' || c.name as club_name
from athlete a
         join club c on a.club_id = c.id;