#include <bits/stdc++.h>

using namespace std;

void solve() {
	int n;
	cin>>n;
	n *= 2;
	vector<int> arr(n);
	for(int i = 0; i<n; i++) {
		cin>>arr[i];
	}
	sort(arr.begin(), arr.end());
	for(int i = 0; i<n-1; i++) {
		int x = arr[i]+arr[n-1];
		multiset<int> ts(arr.begin(), arr.end());
		vector<pair<int, int>> ans;
		bool valid = true;
		while(!ts.empty()) {
			auto last = --ts.end();
			int val = *last;
			ts.erase(last);
			auto cur = ts.find(x-val);
			if(cur==ts.end()) {
				valid = false;
				break;
			}
			ans.emplace_back(val, x-val);
			x = max(val, x-val);
			ts.erase(cur);
		}
		if(valid) {
			cout<<"YES\n"<<arr[i]+arr[n-1]<<"\n";
			for(auto &a: ans) {
				cout<<a.first<<" "<<a.second<<"\n";
			}
			return;
		}
	}
	cout<<"NO\n";
}

signed main() {
	ios_base::sync_with_stdio(0);
	cin.tie(0);
	int kase;
	cin>>kase;
	while(kase--) {
		solve();
	}
}

//CHECK FOR LONG LONG!!!
//LONG LONG OVERFLOW??
//TLE: didnt take in all the input?
//bitwise operations come super late
//multiset instead of set?
//crashing: not taking input for n or m?
//switch n and m?