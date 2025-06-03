import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Upload, TrendingUp, BarChart3, PieChart } from "lucide-react";
import axios from "axios";
import { axiosClient, BACKEND_URI } from "@/config";
import { useNavigate } from "react-router-dom";
import type { User } from "@/models/User";

export default function Login() {
  const [isSignInOpen, setIsSignInOpen] = useState(false);
  const [isRegisterOpen, setIsRegisterOpen] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const nav = useNavigate();
  axios.defaults.withCredentials = true;

  // If user is already authenticated then we can just redirect to home page
  useEffect(() => {
    // axios.get(`${BACKEND_URI}/auth/validate`).then((response) => {
    //   if (response.status == 200) {
    //     nav("/home");
    //   } 
    // });
  }, []);

  const handleSignInModal = () => {
    setIsSignInOpen(isSignInOpen => !isSignInOpen);
    setEmail("");
    setPassword("");
  }

  const handleRegisterModal = () => {
    setIsRegisterOpen(isRegisterOpen => !isRegisterOpen);
    setEmail("");
    setPassword("");
  }

  const validateCreds = (email: string, password: string): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    return password.length > 8 && emailRegex.test(email);
  }

  const handleRegisterCall = () => {
    if (validateCreds(email, password)) {
      const user: User = {
        email: email,
        password: password
      }

      axios.post("/auth/register", user)
        .then((response) => {
          console.log(response);
          if (response.status == 200) {
            nav("/home")
          } else {
            alert("Client Error");
          }
        })
        .catch((e) => {
          console.log(e);
          alert("User already exists")
        });

    } else {
      alert("Please user a correct email and password > 8 characters")
    }
  }

  const handleSignInCall = () => {
    if (validateCreds(email, password)) {
      const user: User = {
        email: email,
        password: password
      }

      axiosClient.post(BACKEND_URI + "/auth/signIn", user)
        .then((response) => {
          if (response.status == 200) {
            nav("/home")
          } else {
            alert("Client Error");
          }
        })
        .catch(() => {
          console.log(BACKEND_URI + "/auth/signIn")
          alert("User not found")
        });

    } else {
      alert("Please user a correct email and password > 8 characters")
    }
  }

  return (
    <div className="bg-black w-screen m-0 p-0 overflow-x-hidden">
      {/* Header */}
      <header className="border-b border-green-500/20 bg-black">
        <div className="container mx-auto px-4 py-6">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <div className="h-8 w-8 rounded-lg bg-green-500 flex items-center justify-center">
                <BarChart3 className="h-5 w-5 text-black" />
              </div>
              <h1 className="text-2xl font-bold text-white">
                Financial Tracker
              </h1>
            </div>
            <div className="flex space-x-3">
              <Dialog open={isSignInOpen} onOpenChange={handleSignInModal}>
                <DialogTrigger asChild>
                  <Button
                    variant="outline"
                    className="border-white text-white hover:bg-white hover:text-black"
                  >
                    Sign In
                  </Button>
                </DialogTrigger>
                <DialogContent className="bg-black border-green-500">
                  <DialogHeader>
                    <DialogTitle className="text-white">Sign In</DialogTitle>
                    <DialogDescription className="text-gray-400">
                      Enter your credentials to access your account
                    </DialogDescription>
                  </DialogHeader>
                  <div className="space-y-4">
                    <div className="space-y-2">
                      <Label htmlFor="signin-email" className="text-white">
                        Email
                      </Label>
                      <Input
                        id="signin-email"
                        type="email"
                        placeholder="your@email.com"
                        className="bg-black border-white text-white placeholder:text-gray-400 focus:border-green-500"
                        onChange={(e) => setEmail(e.target.value)}
                      />
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="signin-password" className="text-white">
                        Password
                      </Label>
                      <Input
                        id="signin-password"
                        type="password"
                        className="bg-black border-white text-white focus:border-green-500"
                        onChange={(e) => setPassword(e.target.value)}
                      />
                    </div>
                    <Button 
                    className="w-full bg-green-500 hover:bg-green-600 text-black font-semibold"
                    onClick={handleSignInCall}
                    >
                      Sign In
                    </Button>
                    <div className="text-center">
                      <button
                        className="text-sm text-green-500 hover:text-green"
                        onClick={() => {
                          setIsSignInOpen(false);
                          setIsRegisterOpen(true);
                        }}
                      >
                        Don't have an account? Register
                      </button>
                    </div>
                  </div>
                </DialogContent>
              </Dialog>

              <Dialog open={isRegisterOpen} onOpenChange={handleRegisterModal}>
                <DialogTrigger asChild>
                  <Button className="bg-green-500 hover:bg-green-600 font-semibold text-white">
                    Register
                  </Button>
                </DialogTrigger>
                <DialogContent className="bg-black border-green-500">
                  <DialogHeader>
                    <DialogTitle className="text-white">
                      Create Account
                    </DialogTitle>
                    <DialogDescription className="text-gray-400">
                      Join Financial Tracker to start managing your finances
                    </DialogDescription>
                  </DialogHeader>
                  <div className="space-y-4">
                    <div className="grid grid-cols-2 gap-4"></div>
                    <div className="space-y-2">
                      <Label htmlFor="register-email" className="text-white">
                        Email
                      </Label>
                      <Input
                        id="register-email"
                        type="email"
                        placeholder="your@email.com"
                        className="bg-black border-white text-white placeholder:text-gray-400 focus:border-green-500"
                      />
                    </div>
                    <div className="space-y-2">
                      <Label htmlFor="register-password" className="text-white">
                        Password
                      </Label>
                      <Input
                        id="register-password"
                        type="password"
                        className="bg-black border-white text-white focus:border-green-500"
                      />
                    </div>
                    <Button 
                    className="w-full bg-green-500 hover:bg-green-600 text-black font-semibold"
                    onClick={handleRegisterCall}
                    >
                      Create Account
                    </Button>
                    <div className="text-center">
                      <button
                        className="text-sm text-green-500 hover:text-green-400"
                        onClick={() => {
                          setIsRegisterOpen(false);
                          setIsSignInOpen(true);
                        }}
                      >
                        Already have an account? Sign In
                      </button>
                    </div>
                  </div>
                </DialogContent>
              </Dialog>
            </div>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <main className="container mx-auto px-4 py-16">
        <div className="text-center mb-16">
          <h2 className="text-5xl font-bold text-white mb-6">
            Take Control of Your
            <span className="text-green-500"> Finances</span>
          </h2>
          <p className="text-xl text-gray-300 max-w-2xl mx-auto mb-8">
            Upload bank statements, connect your accounts, and get powerful
            insights into your financial health with our modern tracking
            platform.
          </p>
          <div className="flex justify-center space-x-4">
            <Button
              size="lg"
              className="bg-green-500 text-white hover:bg-green-600 font-semibold text-lg px-8"
              onClick={() => setIsRegisterOpen(true)}
            >
              Register
            </Button>
            <Button
              size="lg"
              variant="outline"
              className="border-white text-white hover:bg-white hover:text-black text-lg px-8"
              onClick={() => setIsSignInOpen(true)}
            >
              Sign In
            </Button>
          </div>
        </div>

        {/* Features Grid */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8 mb-16">
          <Card className="bg-black border-white hover:border-green-500 transition-colors">
            <CardHeader>
              <div className="h-12 w-12 rounded-lg bg-green-500 flex items-center justify-center mb-4">
                <Upload className="h-6 w-6 text-black" />
              </div>
              <CardTitle className="text-white">
                Upload Bank Statements
              </CardTitle>
              <CardDescription className="text-gray-400">
                Securely upload your bank statements in PDF or CSV format for
                automatic transaction parsing and categorization.
              </CardDescription>
            </CardHeader>
          </Card>

          <Card className="bg-black border-white hover:border-green-500 transition-colors">
            <CardHeader>
              <div className="h-12 w-12 rounded-lg bg-green-500 flex items-center justify-center mb-4">
                <TrendingUp className="h-6 w-6 text-black" />
              </div>
              <CardTitle className="text-white">Smart Analytics</CardTitle>
              <CardDescription className="text-gray-400">
                Get intelligent insights into your spending patterns, budget
                tracking, and financial trends with AI-powered analysis.
              </CardDescription>
            </CardHeader>
          </Card>

          <Card className="bg-black border-white hover:border-green-500 transition-colors">
            <CardHeader>
              <div className="h-12 w-12 rounded-lg bg-green-500 flex items-center justify-center mb-4">
                <PieChart className="h-6 w-6 text-black" />
              </div>
              <CardTitle className="text-white">Budget Management</CardTitle>
              <CardDescription className="text-gray-400">
                Set spending limits, track budget goals, and receive alerts when
                you're approaching your limits.
              </CardDescription>
            </CardHeader>
          </Card>
        </div>
      </main>

      {/* Footer */}
      <footer className="border-t border-green-500/20 bg-black mt-16">
        <div className="container mx-auto px-4 py-8">
          <div className="text-center text-gray-400">
            <p>&copy; Financial Tracker</p>
          </div>
        </div>
      </footer>
    </div>
  );
}
