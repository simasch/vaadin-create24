create view athlete_view as
select a.id, a.first_name, a.last_name, c.abbreviation || ' ' || c.name as club_name
from athlete a
         join club c on a.club_id = c.id;

comment on view athlete_view is 'Athlete with club abbreviation and name';

comment on column athlete_view.id is 'Athlete ID';
comment on column athlete_view.first_name is 'Athlete first name';
comment on column athlete_view.last_name is 'Athlete last name';
comment on column athlete_view.club_name is 'Club abbreviation and name concatenated';