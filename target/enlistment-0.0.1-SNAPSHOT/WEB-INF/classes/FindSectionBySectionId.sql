SELECT section_id,subject_id,schedule,faculty_number,f.firstname,f.lastname,room_name,capacity,
(select array(select student_number from enlistments e where s.section_id = e.section_id)) AS students,
s.version
FROM sections s 
LEFT OUTER JOIN rooms r 
USING (room_name) 
LEFT OUTER JOIN faculty f 
USING (faculty_number) 
where section_id = ?
