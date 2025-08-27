package org.ums.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "grade_items",
       uniqueConstraints = @UniqueConstraint(name = "uq_grade_item", columnNames = {"class_id", "item_type", "item_id"}))
public class GradeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity clazz;

    @Column(name = "item_type", nullable = false, length = 30)
    private String itemType;

    @Column(name = "item_id", nullable = false, length = 100)
    private String itemId;

    @Column(name = "max_score", nullable = false, precision = 10, scale = 2)
    private BigDecimal maxScore;

    @Column(name = "assigned_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime assignedAt;

    public Long getId() {
        return id;
    }

    public ClassEntity getClazz() {
        return clazz;
    }

    public GradeItem setClazz(ClassEntity clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public GradeItem setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getItemId() {
        return itemId;
    }

    public GradeItem setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public BigDecimal getMaxScore() {
        return maxScore;
    }

    public GradeItem setMaxScore(BigDecimal maxScore) {
        this.maxScore = maxScore;
        return this;
    }

    public OffsetDateTime getAssignedAt() {
        return assignedAt;
    }

    public GradeItem setAssignedAt(OffsetDateTime assignedAt) {
        this.assignedAt = assignedAt;
        return this;
    }
}