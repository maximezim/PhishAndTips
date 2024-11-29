declare module 'zxcvbn' {
    interface ZXCVBNResult {
      score: number;
      feedback: {
        suggestions: string[];
        warning: string;
      };
      crack_times_display: {
        offline_slow_hashing_1e4_per_second: string;
        [key: string]: string;
      };
      [key: string]: any;
    }
  
    function zxcvbn(password: string): ZXCVBNResult;
  
    export = zxcvbn;
  }