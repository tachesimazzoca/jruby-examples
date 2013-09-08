#!/usr/bin/env ruby

require 'erb'

params = Array.new(20).map do
  { :title => "foo", :description => "bar" }
end
erb = File.read(File.expand_path('../template.erb',  __FILE__))

start = Time.now
1000.times do
  puts ERB.new(erb, nil, '-').result(binding)
end
puts (Time.now - start).to_s
