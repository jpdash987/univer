
@RestController
@RequestMapping("/api/admin/admissions")
public class AdmissionsController {
    private final EventPublisher events;
    private final ObjectMapper mapper = new ObjectMapper();

    public AdmissionsController(EventPublisher events) {
        this.events = events;
    }

    public record ApprovalRequest(Long studentId, Long admissionId, String decision) {}

    @PostMapping("/approve")
    public String approve(@RequestBody ApprovalRequest req) throws Exception {
        var payload = mapper.writeValueAsString(req);
        events.publish(DomainEvent.of("AdmissionApproved", req.studentId().toString(), payload, null));
        return "OK";
    }
}