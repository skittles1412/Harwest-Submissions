/**
 * code generated by JHelper
 * More info: https://github.com/AlexeyDmitriev/JHelper
 * @author
 */


#include <bits/stdc++.h>

using namespace std;

class AWatermelon {
public:
	void solve(istream &in, ostream &out) {
		int w;
		in>>w;
		if(w<4||w&1) {
			out<<"NO\n";
		}else {
			out<<"YES\n";
		}
	}
};


int main() {
	ios_base::sync_with_stdio(false);
	cin.tie(NULL);
	AWatermelon solver;
	std::istream& in(std::cin);
	std::ostream& out(std::cout);
	solver.solve(in, out);
	return 0;
}
