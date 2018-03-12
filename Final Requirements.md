Student must be able to enlist or cancel enlistment in sections. Finish any unfinished work, if any.
Each student logs in via his/her student number. No need for password.
System should show student's full name & student number.
Show success or error messages.
For example, if student tries to enlist in section that has schedule conflict with his other sections, system should tell the student that's the reason why enlistment failed.

.


Administrator logs in via his/her Admin ID (positive integer). No need for password.
Administrator sees lists of subjects, rooms, days & periods, as well as a list of existing sections.
Administrator creates a section by choosing a subject, room, days & periods, and by specifying a section ID.
If section creation is successful, system adds section to list of sections.
If section creation is unsuccessful, system stays/returns to same page and informs administrator why section creation failed.
Section creation will fail due to:
Existing section ID.
No two sections can occupy the same room at the same time.
You will need to edit the domain model.
No faculty can teach two classes at the same time.
You will need to edit the domain model.
You will need to create a CreateSectionService class in com.orangeandbronze.enlistment.service.
Create JDBC implementations for the DAO interfaces.
You may modify any of the existing code, inclusive of changing the method signatures.
Refactor.
