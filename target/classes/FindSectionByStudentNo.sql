SELECT section_id,subject_id,schedule,room_name,capacity,faculty_number,faculty_number,f.firstname,f.lastname,se.version
FROM enlistments
LEFT OUTER JOIN students st
using (student_number)
LEFT OUTER JOIN sections se
using (section_id)
LEFT OUTER JOIN rooms r
using (room_name)
LEFT OUTER JOIN faculty f
using (faculty_number)
WHERE st.student_number = ?
