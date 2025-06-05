package com.football.matches.dto;

import java.math.BigDecimal;

public class StadiumDTO {
    private Long id;
    private String name;
    private Integer capacity;
    private BigDecimal pricePerSeat;

    public StadiumDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public BigDecimal getPricePerSeat() {
        return this.pricePerSeat;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setPricePerSeat(BigDecimal pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof StadiumDTO)) return false;
        final StadiumDTO other = (StadiumDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$capacity = this.getCapacity();
        final Object other$capacity = other.getCapacity();
        if (this$capacity == null ? other$capacity != null : !this$capacity.equals(other$capacity)) return false;
        final Object this$pricePerSeat = this.getPricePerSeat();
        final Object other$pricePerSeat = other.getPricePerSeat();
        if (this$pricePerSeat == null ? other$pricePerSeat != null : !this$pricePerSeat.equals(other$pricePerSeat))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof StadiumDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $capacity = this.getCapacity();
        result = result * PRIME + ($capacity == null ? 43 : $capacity.hashCode());
        final Object $pricePerSeat = this.getPricePerSeat();
        result = result * PRIME + ($pricePerSeat == null ? 43 : $pricePerSeat.hashCode());
        return result;
    }

    public String toString() {
        return "StadiumDTO(id=" + this.getId() + ", name=" + this.getName() + ", capacity=" + this.getCapacity() + ", pricePerSeat=" + this.getPricePerSeat() + ")";
    }
}