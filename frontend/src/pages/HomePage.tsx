import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { MetricsCard } from "@/components/metrics-card";
import { StatsChart } from "@/components/stats-chart";
import { LayoutDashboard, Wallet } from "lucide-react";
import { useEffect, useState } from "react";
import { axiosClient, BACKEND_URI } from "@/config";
import { useNavigate } from "react-router-dom";
import {
  bankStatementsSchema,
  type BankStatement,
  type BankStatements,
} from "@/schemas/BankStatement.schema";

export default function HomePage() {
  const nav = useNavigate();
  const [bankStatements, setBankStatements] = useState<BankStatement[]>([]);
  const [balance, setBalance] = useState<number>(0);
  const [balanceDif, setBalanceDif] = useState<number>(0);
  const [deposit, setDeposit] = useState<number>(0);
  const [withdrawals, setWithdrawal] = useState<number>(0);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [graphData, setGraphData] = useState<any[]>([]);
  const MONTHS: Record<string, number> = {
    JAN: 0,
    FEB: 1,
    MAR: 2,
    APR: 3,
    MAY: 4,
    JUN: 5,
    JUL: 6,
    AUG: 7,
    SEP: 8,
    OCT: 9,
    NOV: 10,
    DEC: 11,
  };

  useEffect(() => {
    const startupSequence = async () => {
      await axiosClient
        .get(`${BACKEND_URI}/auth/signIn`)
        .then((response) => console.log(response))
        .catch(() => nav("/"));

      return await axiosClient 
        .get(`${BACKEND_URI}/bankstatement/statements`)
        .then((response) => {
          if (response.status == 200) {
            return bankStatementsSchema.parse(response.data.bankStatements);
          } else {
            return null;
          }
        })
        .catch((error) => console.log(error));
    };
    startupSequence().then((response) => {
      if (response) {
        const sortedStatements = [... response].sort((a, b) => {
          const dataA = parseStatementRange(a.statementRange)?.getTime() || 0;
          const dataB = parseStatementRange(b.statementRange)?.getTime() || 0;
          return dataA - dataB;
        })
        
        if (sortedStatements.length > 0) {
          setBalance(sortedStatements[sortedStatements.length - 1].finalAmount)
        } 

        if (sortedStatements.length > 0) {
          const statement = sortedStatements[sortedStatements.length - 1];
          setBalanceDif(statement.finalAmount - statement.initialAmount);
          setWithdrawal(statement.withdrawals);
          setDeposit(statement.deposits);
        } 

        const data = findNetChanges(sortedStatements);

        setGraphData(data);
        setBankStatements(sortedStatements);
        console.log(bankStatements)
      }
    });
  }, []);

  const parseStatementRange = (range: string) => {
    const match = range.match(/-\s([A-Z]{3}) (\d{2})\/(\d{2})$/)
    if (!match) return null;

    const [, monthStr, dayStr, yearStr] = match;
    const month = MONTHS[monthStr];
    const day = parseInt(dayStr);
    const year = 2000 + parseInt(yearStr, 10);

    return new Date(year, month, day);
  }

  const findNetChanges = (statements: BankStatements) => {
    const data: { date: string; value: number; }[] = []

    statements.forEach((state) => {
      const finalAmount = state.finalAmount;
      const month = state.statementRange;
      const element = {
        date: month,
        value: finalAmount
      }
      data.push(element)
    })

    return data;
  }

  // TODO: Make the data follow some sort of schema
  return (
    <div className="min-h-screen bg-black text-white">
      <div className="grid lg:grid-cols-[280px_1fr]">
        <aside className="border-r bg-black backdrop-blur">
          <div className="flex h-16 items-center gap-2 border-b px-6">
            <Wallet className="h-6 w-6" />
            <span className="font-bold">Finance Tracker</span>
          </div>
          <div className="px-4 py-4">
            <Input placeholder="Search" className="bg-black" />
          </div>
          <nav className="space-y-2 px-2">
            <Button variant="ghost" className="w-full justify-start gap-2">
              <LayoutDashboard className="h-4 w-4" />
              Dashboard
            </Button>
            {/* <Button variant="ghost" className="w-full justify-start gap-2">
              <BarChart3 className="h-4 w-4" />
              Statistics & Income
            </Button>
            <Button variant="ghost" className="w-full justify-start gap-2">
              <Globe className="h-4 w-4" />
              Market
            </Button>
            <Button variant="ghost" className="w-full justify-start gap-2">
              <Home className="h-4 w-4" />
              Funding
            </Button>
            <Button variant="ghost" className="w-full justify-start gap-2">
              <Wallet className="h-4 w-4" />
              Yield Vaults
              <ChevronDown className="ml-auto h-4 w-4" />
            </Button>
            <Button variant="ghost" className="w-full justify-start gap-2">
              <LifeBuoy className="h-4 w-4" />
              Support
            </Button>
            <Button variant="ghost" className="w-full justify-start gap-2">
              <Settings className="h-4 w-4" />
              Settings
            </Button> */}
          </nav>
        </aside>
        <main className="p-6">
          <div className="mb-6 flex items-center justify-between">
            <div className="space-y-1">
              <h1 className="text-2xl font-bold">Overview</h1>
            </div>
          </div>
          <div className="grid gap-4 md:grid-cols-3">
            <MetricsCard
              title="Your Balance"
              value={`$${balance.toLocaleString()}`}
              change={{
                value: balanceDif > 0 ? `+${balanceDif.toLocaleString()}` : `-${balanceDif.toLocaleString()}`,
                percentage: `${((balanceDif / balance) * 100).toFixed(2)}%`,
                isPositive: balanceDif > 0,
              }}
            />
            <MetricsCard
              title="Your Net Deposits"
              value={`$${deposit.toLocaleString()}`}
              change={{
                value: "",
                percentage: "",
                isPositive: true,
              }}
            />
            <MetricsCard
              title="Your Net Withdrawals"
              value={`$${withdrawals.toLocaleString()}`}
              change={{
                value: "",
                percentage: "",
                isPositive: true,
              }}
            />
          </div>
          <Card className="mt-6 p-6 bg-black">
            <div className="mb-4 flex items-center justify-between">
              <h2 className="text-lg font-semibold text-white">
                General Statistics
              </h2>
              <div className="flex gap-2">
                <Button size="sm" variant="ghost">
                  <p className="text-white">Today</p>
                </Button>
                <Button size="sm" variant="ghost">
                  <p className="text-white">Last Week</p>
                </Button>
                <Button size="sm" variant="ghost">
                  <p className="text-white">Last Month</p>
                </Button>
                <Button size="sm" variant="ghost">
                  <p className="text-white">Last 6 Months</p>
                </Button>
                <Button size="sm" variant="ghost">
                  <p className="text-white">Year</p>
                </Button>
              </div>
            </div>
            <div className="outline-solid border rounded-4xl outline-accent/50">
              <StatsChart data={graphData} />
            </div>
          </Card>
          <div className="mt-6">{/* <VaultTable /> */}</div>
        </main>
      </div>
    </div>
  );
}
