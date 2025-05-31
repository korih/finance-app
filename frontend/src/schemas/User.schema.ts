import { z } from "zod";

export const emailSchema = z.string().email();

export const userSchema = z.object({
  email: emailSchema,
  password: z.string(),
}) 

export type User = z.infer<typeof userSchema>; 
export type Email = z.infer<typeof emailSchema>; 