library ieee;
use ieee.std_logic_1164.all;

entity adder is
  port (
    a, b : in std_logic_vector(7 downto 0);
    sum : out std_logic_vector(7 downto 0)
  );
end entity;

architecture behavioral of adder is
begin
  sum <= a + b;
end architecture;