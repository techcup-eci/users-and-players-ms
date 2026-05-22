    Requirements
===========

Functional Requirements
-----------------------

1. Query user profile — The authenticated user can view their personal information registered in the system such as name, email, program and status.
2. Update basic user information — The user can modify their personal data such as name, school relation and semester, except email and password.
3. Deactivate user — The administrator can deactivate a user as long as they are not linked to a team in an active or in-progress tournament.
4. List and search users — The administrator can query and filter users by name, email, school relation or status in a paginated way.
5. Create sports profile — The player can register their sports profile indicating position, jersey number and photo, storing the image in MongoDB.
6. Query sports profile — Players, captains and organizers can view the complete sports profile of a player including their photo and team linkage status.
7. Update sports profile — The player can modify their position, jersey number or photo as long as they are not linked to a team in an active tournament.
8. Block sports profile deletion — The system rejects any attempt to delete a sports profile to preserve the historical integrity of the tournament.
9. Search players to invite — The captain can search registered players by name and view only their availability status to decide who to send a team invitation to.
10. Send team linkage request — A player can send a single active request to join an available team, notifying the Teams Service.
11. Query sent requests — The player can view the current status and history of their team linkage requests.
12. Cancel linkage request — The player can cancel their active request as long as it is in pending status, becoming available to send a new one.
13. Receive team invitation — The system registers invitations sent by captains from the Teams Service to available players.
14. Query received invitations — The player can view all invitations they have received from teams along with their current status.
15. Accept or reject invitation — The player can respond to a pending invitation, confirming team linkage or discarding it and maintaining their availability.
16. Register audit log — The system automatically registers all relevant actions on users and players with user, action, timestamp and modified data.
17. Display user profile view — The frontend displays a screen where the authenticated user can view and edit their personal information in a clear and intuitive form.
18. Display sports profile view — The frontend displays a screen where the player can create, view and update their sports profile including photo upload with preview.
19. Display player search view — The frontend displays a search screen for captains to find players by name and view their availability status with an option to send an invitation directly.
20. Display requests and invitations view — The frontend displays a unified screen where the player can see their sent requests and received invitations with their statuses and action buttons to accept or reject.
21. Display user management view — The frontend displays an administration panel where the administrator can search, filter, view and deactivate users from a paginated table.

Functional Requirement Specifications
------------------------------------

## RF-01 Query user profile

| Field | Description |
|-------|-------------|
| **ID** | RF-01 |
| **Requirement Name** | Query user profile |
| **Description** | The system must allow an authenticated user to view their own personal information registered in the system. |
| **Preconditions** | The user must be authenticated with a valid JWT. The user must exist in the system. |
| **Actor** | Student, Graduate, Professor, Administrative Staff, Family Member, Administrator |
| **Main Flow** | 1. The actor sends a GET request to /users/{id} with their JWT. 2. The system validates the token and verifies the ID belongs to the authenticated user or an administrator. 3. The system queries PostgreSQL. 4. The system returns full name, email, school relation, academic program, semester, status, birth date, and identification. |
| **Use Case Diagram** | ![alt text](./../images/RF1.png) ![alt text](./../images/RF1-.png) |
| **Postconditions** | The user receives their personal information. The action is recorded in the audit log. |

---

## RF-02 Update basic user information

| Field | Description |
|-------|-------------|
| **ID** | RF-02 |
| **Requirement Name** | Update basic user information |
| **Description** | The system must allow an authenticated user or administrator to update basic personal data, excluding email and password. |
| **Preconditions** | The user must be authenticated with a valid JWT. The user must exist and be Active. |
| **Actor** | Student, Graduate, Professor, Administrative Staff, Family Member, Administrator |
| **Main Flow** | 1. The actor sends a PUT request to /users/{id} with fields to update. 2. The system validates the JWT belongs to the user or an administrator. 3. The system validates email and password are not modified. 4. The system validates semester is only sent when relation is student. 5. The system updates data in PostgreSQL. 6. The system records the action in audit and returns the updated data. |
| **Use Case Diagram** | ![alt text](./../images/RF2.png) ![alt text](./../images/RF2-.png) |
| **Postconditions** | The user data is updated in the database. The action is recorded in the audit log. |

---

## RF-03 Deactivate user

| Field | Description |
|-------|-------------|
| **ID** | RF-03 |
| **Requirement Name** | Deactivate user |
| **Description** | The system must allow an administrator to set a user status to Inactive if the user is not linked to a team in an Active or In-Progress tournament. |
| **Preconditions** | The actor must have the Administrator role. The user to deactivate must exist and be Active. |
| **Actor** | Administrator |
| **Main Flow** | 1. The administrator sends PATCH to /users/{id}/status with {status: "INACTIVE"}. 2. The system validates the role. 3. The system queries the Teams Service to check active/in-progress linkage. 4. If no active linkage, the system updates status to INACTIVE in PostgreSQL. 5. The system records the action in audit and returns confirmation. |
| **Use Case Diagram** | ![alt text](./../images/RF3.png) ![alt text](./../images/RF3-.png) |
| **Postconditions** | The user is marked INACTIVE in the database. The action is recorded in audit. |

---

## RF-04 List and search users

| Field | Description |
|-------|-------------|
| **ID** | RF-04 |
| **Requirement Name** | List and search users |
| **Description** | The system must allow an administrator to query users with filters by name, email, school relation, and status. |
| **Preconditions** | The actor must have the Administrator role with a valid JWT. |
| **Actor** | Administrator |
| **Main Flow** | 1. The administrator sends GET to /users with optional parameters: name, email, relation, status. 2. The system validates the role. 3. The system applies filters to PostgreSQL. 4. The system returns a paginated list of users with basic data. |
| **Use Case Diagram** | ![alt text](./../images/RF4.png) ![alt text](./../images/RF4-.png) |
| **Postconditions** | The administrator receives the filtered, paginated list of users. |

---

## RF-05 Create sports profile

| Field | Description |
|-------|-------------|
| **ID** | RF-05 |
| **Requirement Name** | Create sports profile |
| **Description** | The system must allow a user with Player role to create a sports profile with position, jersey number, and photo. Each user can have only one sports profile. |
| **Preconditions** | The actor must be authenticated with a valid JWT and have Player role. The user must not have an existing sports profile. |
| **Actor** | Player |
| **Main Flow** | 1. The player sends POST to /players/profile with position, jersey number, and photo. 2. The system validates Player role. 3. The system verifies no existing profile. 4. The system stores profile data in PostgreSQL. 5. The system stores the photo in MongoDB. 6. The system returns the created profile with 201 Created. 7. The action is recorded in audit. |
| **Use Case Diagram** | ![alt text](./../images/RF5.png) ![alt text](./../images/RF5-.png) |
| **Postconditions** | The sports profile is stored in PostgreSQL and the photo in MongoDB. The action is recorded in audit. |

---

## RF-06 Query sports profile

| Field | Description |
|-------|-------------|
| **ID** | RF-06 |
| **Requirement Name** | Query sports profile |
| **Description** | The system must allow querying a player sports profile, including position, jersey number, photo, and team linkage status. |
| **Preconditions** | The actor must be authenticated with a valid JWT. The sports profile must exist. |
| **Actor** | Player, Captain, Organizer, Administrator |
| **Main Flow** | 1. The actor sends GET to /players/profile/{userId}. 2. The system validates the token and role. 3. The system retrieves profile data from PostgreSQL. 4. The system retrieves the photo from MongoDB. 5. The system returns the assembled profile. |
| **Use Case Diagram** | ![alt text](./../images/RF6.png) ![alt text](./../images/RF6-.png) |
| **Postconditions** | The actor receives the full sports profile information. |

---

## RF-07 Update sports profile

| Field | Description |
|-------|-------------|
| **ID** | RF-07 |
| **Requirement Name** | Update sports profile |
| **Description** | The system must allow a player to update position, jersey number, or photo as long as they are not linked to a team in an Active or In-Progress tournament. |
| **Preconditions** | The actor must be authenticated with a valid JWT and have Player role. The sports profile must exist. |
| **Actor** | Player |
| **Main Flow** | 1. The player sends PUT to /players/profile with fields to modify. 2. The system validates token and role. 3. The system checks Teams Service for Active/In-Progress linkage. 4. If no lock, the system updates PostgreSQL. 5. If a new photo is provided, the system updates MongoDB. 6. The system returns the updated profile. 7. The action is recorded in audit. |
| **Use Case Diagram** | ![alt text](./../images/RF7.png) ![alt text](./../images/RF7-.png) |
| **Postconditions** | The sports profile is updated in PostgreSQL and/or MongoDB. The action is recorded in audit. |

---

## RF-08 Block sports profile deletion

| Field | Description |
|-------|-------------|
| **ID** | RF-08 |
| **Requirement Name** | Block sports profile deletion |
| **Description** | The system must not allow sports profile deletion to preserve the historical integrity of the tournament. |
| **Preconditions** | N/A |
| **Actor** | Any actor |
| **Main Flow** | 1. The actor sends DELETE to /players/profile/{userId}. 2. The system returns 405 Method Not Allowed with a message indicating deletion is not permitted. |
| **Use Case Diagram** | ![alt text](./../images/RF8.png) ![alt text](./../images/RF8-.png) |
| **Postconditions** | The sports profile remains unchanged in the database. |

---

## RF-09 Search players to invite

| Field | Description |
|-------|-------------|
| **ID** | RF-09 |
| **Requirement Name** | Search players to invite |
| **Description** | The system must allow a captain to search registered players by name and view their availability status to decide who to invite. |
| **Preconditions** | The actor must be authenticated with a valid JWT and have Captain role. |
| **Actor** | Captain |
| **Main Flow** | 1. The captain sends GET to /players?name= with the player name. 2. The system validates token and role. 3. The system queries PostgreSQL for matching players. 4. The system returns the list with names and availability status. |
| **Use Case Diagram** | ![alt text](./../images/RF9.png) ![alt text](./../images/RF9-.png) |
| **Postconditions** | The captain receives the player list with availability status. |

---

## RF-10 Send team linkage request

| Field | Description |
|-------|-------------|
| **ID** | RF-10 |
| **Requirement Name** | Send team linkage request |
| **Description** | The system must allow a player to send a request to join an available team, allowing only one active request at a time. |
| **Preconditions** | The actor must be authenticated with Player role. The player must not be linked to a team. The player must not have a pending request. |
| **Actor** | Player |
| **Main Flow** | 1. The player sends POST to /players/requests with the team ID. 2. The system validates token and role. 3. The system verifies no pending request exists. 4. The system verifies the player is not already in a team. 5. The system creates a request with PENDING status in PostgreSQL. 6. The system notifies the Teams Service. 7. The system returns the created request with 201 status. |
| **Use Case Diagram** | ![alt text](./../images/RF10.png) ![alt text](./../images/RF10-.png) |
| **Postconditions** | The request is stored with PENDING status. The Teams Service is notified. The action is recorded in audit. |

---

## RF-11 Query sent requests

| Field | Description |
|-------|-------------|
| **ID** | RF-11 |
| **Requirement Name** | Query sent requests |
| **Description** | The system must allow a player to view the current status and history of their team linkage requests. |
| **Preconditions** | The actor must be authenticated with Player role. |
| **Actor** | Player |
| **Main Flow** | 1. The player sends GET to /players/requests. 2. The system validates the token. 3. The system returns the active request (if any) and the history of previous requests with statuses and dates. |
| **Use Case Diagram** | ![alt text](./../images/RF11.png) ![alt text](./../images/RF11-.png) |
| **Postconditions** | The player receives the updated request status and history. |

---

## RF-12 Cancel linkage request

| Field | Description |
|-------|-------------|
| **ID** | RF-12 |
| **Requirement Name** | Cancel linkage request |
| **Description** | The system must allow a player to cancel a linkage request while it is in PENDING status. |
| **Preconditions** | The actor must be authenticated with Player role. A PENDING request must exist for the player. |
| **Actor** | Player |
| **Main Flow** | 1. The player sends PATCH to /players/requests/{id}/cancel. 2. The system validates the token and request ownership. 3. The system verifies the request is PENDING. 4. The system updates status to CANCELED in PostgreSQL. 5. The system notifies the Teams Service. 6. The system returns confirmation. |
| **Use Case Diagram** | ![alt text](./../images/RF12.png) ![alt text](./../images/RF12-.png) |
| **Postconditions** | The request is marked CANCELED. The player is available to send a new request. The action is recorded in audit. |

---

## RF-13 Receive team invitation

| Field | Description |
|-------|-------------|
| **ID** | RF-13 |
| **Requirement Name** | Receive team invitation |
| **Description** | The system must allow the Teams Service to register an invitation for a player, visible for player management. |
| **Preconditions** | The invited player must exist and be Active. The player must not be linked to a team. The request must come from the authenticated Teams Service. |
| **Actor** | Teams Service (internal call), Captain (indirect) |
| **Main Flow** | 1. The Teams Service sends POST to /players/invitations with player and team IDs. 2. The system validates the request. 3. The system verifies the player is not in a team. 4. The system creates the invitation with PENDING status in PostgreSQL. 5. The system returns confirmation with 201 status. |
| **Use Case Diagram** | ![alt text](./../images/RF13.png) ![alt text](./../images/RF13-.png) |
| **Postconditions** | The invitation is stored with PENDING status for the player. |

---

## RF-14 Query received invitations

| Field | Description |
|-------|-------------|
| **ID** | RF-14 |
| **Requirement Name** | Query received invitations |
| **Description** | The system must allow a player to view all invitations received from teams with their current status. |
| **Preconditions** | The actor must be authenticated with Player role. |
| **Actor** | Player |
| **Main Flow** | 1. The player sends GET to /players/invitations. 2. The system validates the token. 3. The system returns all invitations for the player with status, team name, and invitation date. |
| **Use Case Diagram** | ![alt text](./../images/RF14.png) ![alt text](./../images/RF14-.png) |
| **Postconditions** | The player receives the complete invitation list with status. |

---

## RF-15 Accept or reject invitation

| Field | Description |
|-------|-------------|
| **ID** | RF-15 |
| **Requirement Name** | Accept or reject invitation |
| **Description** | The system must allow a player to accept or reject a pending invitation. On acceptance, the system confirms linkage with Teams Service and cancels other pending invitations. |
| **Preconditions** | The actor must be authenticated with Player role. The invitation must exist in PENDING status and belong to the authenticated player. |
| **Actor** | Player |
| **Main Flow** | 1. The player sends PATCH to /players/invitations/{id} with {action: "ACCEPT"} or {action: "REJECT"}. 2. The system validates the token and ownership. 3. The system verifies PENDING status. 4a. If ACCEPT: set status to ACCEPTED, confirm linkage with Teams Service, and reject other pending invitations. 4b. If REJECT: set status to REJECTED. 5. The system returns the updated invitation. 6. The action is recorded in audit. |
| **Use Case Diagram** | ![alt text](./../images/RF15.png) ![alt text](./../images/RF15-.png) |
| **Postconditions** | The invitation is ACCEPTED or REJECTED. If accepted, the player is linked to the team and other invitations are rejected. The action is recorded in audit. |

---

## RF-16 Register audit log

| Field | Description |
|-------|-------------|
| **ID** | RF-16 |
| **Requirement Name** | Register audit log |
| **Description** | The system must automatically register all relevant actions on users and players for traceability. |
| **Preconditions** | The system is running. The audited action was executed successfully. |
| **Actor** | System |
| **Main Flow** | 1. After relevant actions (user update, deactivation, profile create/update, request/invitation changes), the system writes an audit_log record with user ID, action, timestamp, previous data snapshot, and new data. 2. The administrator can query logs via GET /audit?userId=&action=&from=&to=. |
| **Use Case Diagram** | añadir diagrama de casos de uso |
| **Postconditions** | Each relevant action is stored in audit_log with full metadata. |

---

## RF-17 Display user profile view

| Field | Description |
|-------|-------------|
| **ID** | RF-17 |
| **Requirement Name** | Display user profile view |
| **Description** | The frontend must display a screen where the authenticated user can view and edit their personal information in a clear and intuitive form. |
| **Preconditions** | The user must be authenticated in the frontend and have valid session state. |
| **Actor** | Authenticated user |
| **Main Flow** | 1. The user navigates to the profile screen. 2. The frontend fetches user data from the API. 3. The frontend renders editable fields and shows validation hints. 4. The user saves changes and receives feedback. |
| **Use Case Diagram** | añadir diagrama de casos de uso |
| **Postconditions** | The user can view and submit updates to their profile data. |

---

## RF-18 Display sports profile view

| Field | Description |
|-------|-------------|
| **ID** | RF-18 |
| **Requirement Name** | Display sports profile view |
| **Description** | The frontend must display a screen where the player can create, view, and update their sports profile with photo upload and preview. |
| **Preconditions** | The user must be authenticated and have Player role. |
| **Actor** | Player |
| **Main Flow** | 1. The player opens the sports profile screen. 2. The frontend loads current data (if any). 3. The player uploads a photo and edits fields. 4. The frontend validates inputs and submits changes. |
| **Use Case Diagram** | añadir diagrama de casos de uso |
| **Postconditions** | The player can view and update the sports profile with a photo preview. |

---

## RF-19 Display player search view

| Field | Description |
|-------|-------------|
| **ID** | RF-19 |
| **Requirement Name** | Display player search view |
| **Description** | The frontend must display a search screen for captains to find players by name and see availability with an option to invite directly. |
| **Preconditions** | The user must be authenticated and have Captain role. |
| **Actor** | Captain |
| **Main Flow** | 1. The captain opens the player search view. 2. The captain enters a name filter. 3. The frontend queries the API and displays results with availability status. 4. The captain sends an invitation from the result list. |
| **Use Case Diagram** | añadir diagrama de casos de uso |
| **Postconditions** | The captain can search players and send invitations from the view. |

---

## RF-20 Display requests and invitations view

| Field | Description |
|-------|-------------|
| **ID** | RF-20 |
| **Requirement Name** | Display requests and invitations view |
| **Description** | The frontend must display a unified screen where the player can see sent requests and received invitations with statuses and action buttons. |
| **Preconditions** | The user must be authenticated and have Player role. |
| **Actor** | Player |
| **Main Flow** | 1. The player opens the requests and invitations view. 2. The frontend loads requests and invitations from the API. 3. The frontend displays status chips and action buttons. 4. The player accepts or rejects invitations. |
| **Use Case Diagram** | añadir diagrama de casos de uso |
| **Postconditions** | The player can manage requests and invitations from a single screen. |

---

## RF-21 Display user management view

| Field | Description |
|-------|-------------|
| **ID** | RF-21 |
| **Requirement Name** | Display user management view |
| **Description** | The frontend must display an administration panel where the administrator can search, filter, view, and deactivate users from a paginated table. |
| **Preconditions** | The user must be authenticated and have Administrator role. |
| **Actor** | Administrator |
| **Main Flow** | 1. The administrator opens the user management view. 2. The frontend loads paginated user data. 3. The administrator applies filters and selects a user. 4. The administrator deactivates a user and receives confirmation. |
| **Use Case Diagram** | añadir diagrama de casos de uso |
| **Postconditions** | The administrator can manage users from the panel with pagination. |

Non-Functional Requirements
---------------------------

1. JWT authentication — All endpoints require a valid JWT token generated by the Identity Service, returning 401 for absent, malformed or expired tokens.
2. Role-based access control — Each endpoint validates that the role in the token has permission for the requested operation, returning 403 if the role is insufficient.
3. Secure communication between services — All communication between microservices occurs over HTTPS without exposing sensitive data in logs or error responses.
4. PostgreSQL and MongoDB storage separation — Structured data is persisted in PostgreSQL and sports profile photos exclusively in MongoDB, referenced by ID.
5. Database referential integrity — The schema guarantees no orphan profiles, duplicate active requests or duplicate active invitations for the same player and team exist.
6. Mandatory layered architecture — The microservice strictly separates controller, service, adapter and repository layers with no cross dependencies between non-adjacent layers.
7. Decoupled integration with external microservices — Communication with the Teams Service uses WebClient or Feign Client, handling errors and timeouts with a controlled 503 response.
8. Code coverage with JaCoCo — Minimum test coverage of 80%, with automatic build failure if the configured threshold is not reached.
9. Code quality analysis with SonarQube — The code must pass the Quality Gate with no critical bugs, security vulnerabilities or high severity code smells.
10. API documentation with Swagger/OpenAPI — All endpoints are documented with springdoc-openapi including parameters, request body, response codes and examples.
11. Centralized exception handling — A global @RestControllerAdvice captures all errors returning uniform JSON responses without exposing stack traces.
12. Frontend responsiveness — The user interface must be responsive and functional on desktop and mobile devices, adapting correctly to different screen sizes using the React framework with TypeScript.
13. Frontend input validation — All forms in the frontend must validate required fields, data formats and character limits before sending requests to the API, showing clear error messages to the user.
14. Frontend JWT session management — The frontend must store the JWT token securely, attach it automatically to every API request and redirect the user to the login screen when the token expires.
15. Frontend user experience — Screen transitions, loading states and error or success messages must be handled consistently across all views so the user always knows what state the application is in.

Non-Functional Requirement Specifications
----------------------------------------

## RNF-01 JWT authentication

| Field | Description |
|-------|-------------|
| **ID** | RNF-01 |
| **Requirement Name** | JWT authentication |
| **Description** | All endpoints must require a valid JWT token generated by the Identity Service, returning 401 for absent, malformed, or expired tokens. |
| **Acceptance Criteria** | Requests without a valid token return 401 Unauthorized. The microservice only validates tokens and never issues them. |
| **Category** | Security |

---

## RNF-02 Role-based access control

| Field | Description |
|-------|-------------|
| **ID** | RNF-02 |
| **Requirement Name** | Role-based access control |
| **Description** | Each endpoint must validate that the role in the JWT has permission for the requested operation, returning 403 if the role is insufficient. |
| **Acceptance Criteria** | A valid token with insufficient role returns 403 Forbidden. Role checks are enforced at controller level (e.g., @PreAuthorize). |
| **Category** | Security |

---

## RNF-03 Secure communication between services

| Field | Description |
|-------|-------------|
| **ID** | RNF-03 |
| **Requirement Name** | Secure communication between services |
| **Description** | All communication between microservices must use HTTPS and must not expose sensitive data in logs or error responses. |
| **Acceptance Criteria** | All service-to-service calls use HTTPS. Logs and error responses never include sensitive data (e.g., identifiers, birth dates). |
| **Category** | Security |

---

## RNF-04 PostgreSQL and MongoDB storage separation

| Field | Description |
|-------|-------------|
| **ID** | RNF-04 |
| **Requirement Name** | PostgreSQL and MongoDB storage separation |
| **Description** | Structured data is persisted in PostgreSQL and sports profile photos are stored exclusively in MongoDB, referenced by ID. |
| **Acceptance Criteria** | Sports profile photo data is stored in MongoDB only. PostgreSQL stores the MongoDB document ID and assembles responses by joining both sources. |
| **Category** | Persistence |

---

## RNF-05 Database referential integrity

| Field | Description |
|-------|-------------|
| **ID** | RNF-05 |
| **Requirement Name** | Database referential integrity |
| **Description** | The schema must prevent orphan profiles, duplicate active requests, and duplicate active invitations for the same player and team. |
| **Acceptance Criteria** | Uniqueness constraints and foreign keys enforce integrity at the schema level. Service logic also validates duplicates before insert. |
| **Category** | Persistence |

---

## RNF-06 Mandatory layered architecture

| Field | Description |
|-------|-------------|
| **ID** | RNF-06 |
| **Requirement Name** | Mandatory layered architecture |
| **Description** | The microservice must strictly separate controller, service, adapter, and repository layers with no cross dependencies between non-adjacent layers. |
| **Acceptance Criteria** | Controllers do not access repositories directly. Repositories contain no business logic. Static analysis shows no cross-layer dependencies. |
| **Category** | Architecture |

---

## RNF-07 Decoupled integration with external microservices

| Field | Description |
|-------|-------------|
| **ID** | RNF-07 |
| **Requirement Name** | Decoupled integration with external microservices |
| **Description** | Communication with the Teams Service uses WebClient or Feign Client and handles errors and timeouts with a controlled 503 response. |
| **Acceptance Criteria** | Each external service has a dedicated adapter. Timeouts or errors return 503 Service Unavailable with a descriptive message. |
| **Category** | Architecture |

---

## RNF-08 Code coverage with JaCoCo

| Field | Description |
|-------|-------------|
| **ID** | RNF-08 |
| **Requirement Name** | Code coverage with JaCoCo |
| **Description** | Minimum test coverage is 80%, with build failure if the configured threshold is not reached. |
| **Acceptance Criteria** | JaCoCo reports at least 80% coverage for service layer tests. Maven build fails if coverage drops below the threshold. |
| **Category** | Quality |

---

## RNF-09 Code quality analysis with SonarQube

| Field | Description |
|-------|-------------|
| **ID** | RNF-09 |
| **Requirement Name** | Code quality analysis with SonarQube |
| **Description** | The microservice must pass the SonarQube Quality Gate with no critical or blocker bugs, no reported security vulnerabilities, and no high-severity code smells. |
| **Acceptance Criteria** | The Quality Gate returns PASSED on each integration. No BLOCKER or CRITICAL vulnerabilities exist. Technical debt does not exceed one day per sprint. |
| **Category** | Quality |

---

## RNF-10 API documentation with Swagger/OpenAPI

| Field | Description |
|-------|-------------|
| **ID** | RNF-10 |
| **Requirement Name** | API documentation with Swagger/OpenAPI |
| **Description** | All endpoints must be documented with springdoc-openapi including description, parameters, request body, response codes, and examples. Documentation must be available at /swagger-ui.html in development. |
| **Acceptance Criteria** | Each endpoint includes @Operation, @ApiResponse, and @Parameter annotations. A developer can consume the API without reading source code. |
| **Category** | Maintainability |

---

## RNF-11 Centralized exception handling

| Field | Description |
|-------|-------------|
| **ID** | RNF-11 |
| **Requirement Name** | Centralized exception handling |
| **Description** | A global @RestControllerAdvice must capture errors and return uniform JSON responses without exposing stack traces. |
| **Acceptance Criteria** | Unhandled exceptions return {status, message, timestamp}. Validation errors return 400 with field-level details. No stack traces are exposed in HTTP responses. |
| **Category** | Maintainability |

---

## RNF-12 Frontend responsiveness

| Field | Description |
|-------|-------------|
| **ID** | RNF-12 |
| **Requirement Name** | Frontend responsiveness |
| **Description** | The UI must be responsive on desktop and mobile, adapting correctly to different screen sizes using React with TypeScript. |
| **Acceptance Criteria** | All core views render correctly at common breakpoints (mobile, tablet, desktop) without layout breakage. |
| **Category** | Usability |

---

## RNF-13 Frontend input validation

| Field | Description |
|-------|-------------|
| **ID** | RNF-13 |
| **Requirement Name** | Frontend input validation |
| **Description** | All frontend forms must validate required fields, data formats, and character limits before sending requests to the API. |
| **Acceptance Criteria** | Invalid inputs are blocked client-side with clear error messages. Requests are not sent when validation fails. |
| **Category** | Usability |

---

## RNF-14 Frontend JWT session management

| Field | Description |
|-------|-------------|
| **ID** | RNF-14 |
| **Requirement Name** | Frontend JWT session management |
| **Description** | The frontend must store the JWT securely, attach it to every API request, and redirect to login when the token expires. |
| **Acceptance Criteria** | All API calls include the JWT. Expired tokens trigger a redirect to the login screen and a user-facing message. |
| **Category** | Security |

---

## RNF-15 Frontend user experience

| Field | Description |
|-------|-------------|
| **ID** | RNF-15 |
| **Requirement Name** | Frontend user experience |
| **Description** | Screen transitions, loading states, and error/success messages must be consistent across all views. |
| **Acceptance Criteria** | Each view shows a standardized loading indicator and consistent success/error feedback. Navigation states are predictable. |
| **Category** | Usability |

# Diagrama de contexto
 ![alt text](<../images/Diagrama de contexto Users and Players.png>)

# Diagrama de contenedores
![alt text](<../images/Diagrama contenedores Usuers and Players.png>)

# Diagrama de Entidad Relacion
![alt text](<../images/Diagrama ER users and players.png>)
