import { z } from "zod";

export const bankStatementSchema = z.object({
  id: z.number(),
  statementRange: z.string(),
  date: z.string(),
  version: z.string(),
  initialAmount: z.number(),
  withdrawals: z.number(),
  deposits: z.number(),
  finalAmount: z.number(),
});

export const bankStatementsSchema = z.array(bankStatementSchema);

export type BankStatement = z.infer<typeof bankStatementSchema>;
export type BankStatements = z.infer<typeof bankStatementsSchema>;

