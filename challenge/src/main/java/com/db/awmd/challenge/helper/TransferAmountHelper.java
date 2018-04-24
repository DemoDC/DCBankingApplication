package com.db.awmd.challenge.helper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TransferAmountHelper {

  @NotNull
  @NotEmpty
  private final String accountFromId;

  @NotNull
  @NotEmpty
  private final String accountToId;

  @NotNull
  @Min(value = 0, message = "Transfer balance must be positive.")
  private BigDecimal amountToTransfer;

  @JsonCreator
  public TransferAmountHelper(@JsonProperty("accountFromId") String accountFromId,
                              @JsonProperty("amountToTransfer") BigDecimal amountToTransfer, @JsonProperty("accountToId") String accountToId) {
    this.accountFromId = accountFromId;
    this.amountToTransfer = amountToTransfer;
    this.accountToId = accountToId;
  }
}
