//package com.programmingtechie.productService.config;
//
//import brave.Tracing;
//import brave.propagation.ThreadLocalCurrentTraceContext;
//import brave.sampler.Sampler;
//import io.micrometer.tracing.Tracer;
//import io.micrometer.tracing.brave.bridge.BraveTracer;
//import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
//import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
//import io.micrometer.tracing.brave.bridge.BravePropagator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TracingConfiguration {
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Bean
//    public Tracing braveTracing() {
//        return Tracing.newBuilder()
//                .localServiceName("product-service")
//                .sampler(Sampler.ALWAYS_SAMPLE)
//                .currentTraceContext(ThreadLocalCurrentTraceContext.newBuilder().build())
//                .build();
//    }
//
////    @Bean
////    public BraveCurrentTraceContext braveCurrentTraceContext(Tracing tracing) {
////        return new BraveCurrentTraceContext(tracing.currentTraceContext());
////    }
//    @Bean
//    public BraveBaggageManager braveBaggageManager() {
//        return new BraveBaggageManager();
//    }
//
//    @Bean
//    public BravePropagator bravePropagator(Tracing tracing) {
//        return new BravePropagator(tracing);
//    }
//
//    @Bean
//    public Tracer micrometerTracer(Tracing tracing, BraveBaggageManager baggageManager) {
//        BraveCurrentTraceContext currentTraceContext = (BraveCurrentTraceContext) applicationContext.getBean("braveCurrentTraceContext");
//        return new BraveTracer(tracing.tracer(), currentTraceContext, baggageManager);
//    }
//}
