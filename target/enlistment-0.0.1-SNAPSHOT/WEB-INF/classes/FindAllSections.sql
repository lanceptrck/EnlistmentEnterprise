SELECT section_id,subject_id,schedule,room_name,capacity,faculty_number,f.firstname,f.lastname,version 
FROM sections 
NATURAL JOIN rooms 
NATURAL JOIN faculty f
