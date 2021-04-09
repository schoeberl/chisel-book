module PathBlackBoxAdder(a, b, cin, c, cout);
input  [31:0] a, b;
input  cin;
output [31:0] c;
output cout;
wire   [32:0] sum;

assign sum  = a + b + cin;
assign c    = sum[31:0];
assign cout = sum[32];

endmodule
